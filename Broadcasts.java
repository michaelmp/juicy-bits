package team133;

import battlecode.common.*;

/**
 * A broadcast message is an int[] of packets with length modulo the packet size.
 * {
 *   int[0] := team identifier
 *   ...    := Packet[]
 * }
 * Each packet is an int[PACKET_SIZE] encoded as:
 * {
 *  int[0] := metadata
 *  int[1] := metadata MAC
 *  int[2] := map location x
 *  int[3] := MAC for x
 *  int[4] := map location y
 *  int[5] := MAC for y
 * }
 *
 * Messages are sent in the clear with a MAC for all data.
 *
 * The MAC is computed by padding the payload bits with a one time pad. The message
 * is authenticated by xor-ing the mac and payload to achieve the pad.
 *
 * Every round has a unique one time pad defined in team133.Codex.code_by_round.
 *
 * By battlecode rules, a robot may receive messages the same round they are sent, or the next
 * round, depending on their robot id. To synchronize MACs for all robots:
 * - send on odd rounds with MAC[next even round]
 * - read on even rounds with MAC[current round]
 */
public class Broadcasts {

  // opcodes
  public static final int ATTACK_TO_LOC = 0x0000;
  public static final int DEFEND_AT_LOC = 0xFFFF;
  public static final int BOUND_UP = 0xF000;
  public static final int BOUND_DOWN = 0x0F00;
  public static final int BOUND_LEFT = 0x00F0;
  public static final int BOUND_RIGHT = 0x000F;

  public static class Packet {
    public final int opcode;
    public final MapLocation loc;
    public final MapLocation origin;
    public final MapLocation rebroadcast_origin;
    public Packet(int opcode, MapLocation loc, MapLocation origin, MapLocation rebroadcast_origin) {
      this.opcode = opcode;
      this.loc = loc;
      this.origin = origin;
      if (rebroadcast_origin == null) {
        this.rebroadcast_origin = this.origin;
      } else {
        this.rebroadcast_origin = rebroadcast_origin;
      }
    }
  }

  private final int team_bits;

  public static final int PACKET_SIZE = 8;
  public static final int MAX_OUT = 5;
  public static final int MAX_IN = 5;

  private final RobotController rc;
  private final Bot bot;
  public Broadcasts(Bot bot) {
    this.rc = bot.rc;
    this.bot = bot;
    this.team_bits = (bot.team == Team.A) ? 0xFFFFFFFF : 0x00000000;
  }

  public int packets_out_count = 0;
  public Packet[] packets_out = new Packet[MAX_OUT];
  public void addMessage(Packet p) {
    if (packets_out_count >= MAX_OUT) return;
    for (int i = 0; i < packets_out_count; i++) {
      if (packets_out[i].opcode == p.opcode && packets_out[i].loc == p.loc) return;
    }
    packets_out[packets_out_count++] = p;
  }

  /** TODO: clear duplicates (particularly with respect to rebroadcasts)? */
  public void send() {
    if (Clock.getRoundNum() % 2 == 0 || packets_out_count == 0) return;
    if (rc.hasBroadcasted()) return;
    if (bot.flux < 1) return;
    Message msg = new Message();
    msg.ints = new int[1 + packets_out_count * PACKET_SIZE];
    msg.ints[0] = team_bits;
    for (int i = 0; i < packets_out_count; i++) {
      Packet p = packets_out[i];
      int ii = 1 + i * PACKET_SIZE;
      msg.ints[ii + 1] = p.opcode;
      msg.ints[ii + 2] = p.loc.x;
      msg.ints[ii + 3] = p.loc.y;
      msg.ints[ii + 4] = p.origin.x;
      msg.ints[ii + 5] = p.origin.y;
      msg.ints[ii + 6] = p.rebroadcast_origin.x;
      msg.ints[ii + 7] = p.rebroadcast_origin.y;
      msg.ints[ii + 0] = 0;
      for (int j = 1; j < PACKET_SIZE; j++) {
        msg.ints[ii + 0] += msg.ints[ii + j];
      }
      msg.ints[ii + 0] = encode(msg.ints[ii + 0]);
    }
    try {
      rc.broadcast(msg);
      packets_out_count = 0;
    } catch (GameActionException e) {
      e.printStackTrace();
    }
  }

  private void debug_badMessage(int a, int b) {
    System.out.format("Round %d ||| payload: %x, mac: %x : understood: %x, expected: %x\n",
        Clock.getRoundNum(),
        a,
        b,
        (a ^ b),
        Codex.code_by_round[Clock.getRoundNum() % Codex.code_by_round.length]);
    System.out.format("? prev: %x cur: %x next: %x\n",
        Codex.code_by_round[Clock.getRoundNum() - 1 % Codex.code_by_round.length],
        Codex.code_by_round[Clock.getRoundNum() % Codex.code_by_round.length],
        Codex.code_by_round[Clock.getRoundNum() + 1 % Codex.code_by_round.length]);
  }

  public int packets_in_count = 0;
  public Packet[] packets_in = new Packet[MAX_IN];
  public void recv() {
    if (Clock.getRoundNum() % 2 == 1) {
      return;
    } else {
      packets_in_count = 0;
    }
    outer: for (Message msg : rc.getAllMessages()) {
      if (msg == null) continue outer;
      if (msg.ints == null) continue outer;
      if (msg.ints.length < 1) continue outer;
      if (msg.ints[0] != team_bits) continue outer;
      if ((msg.ints.length - 1) % PACKET_SIZE != 0) continue outer;
      inner: for (int i = 1; i < msg.ints.length; i += PACKET_SIZE) {
        if (packets_in_count >= MAX_IN) return;
        int opcode = msg.ints[i + 1];
        int x = msg.ints[i + 2];
        int y = msg.ints[i + 3];
        int ox = msg.ints[i + 4];
        int oy = msg.ints[i + 5];
        int rox = msg.ints[i + 6];
        int roy = msg.ints[i + 7];
        int code = msg.ints[i + 0];

        int check = 0;
        for (int j= 1; j < PACKET_SIZE; j++) {
          check += msg.ints[i + j];
        }
        if (check != decode(code)) continue outer;

        MapLocation loc = new MapLocation(x, y);
        MapLocation origin = new MapLocation(ox, oy);
        MapLocation rebroadcast_origin = new MapLocation(rox, roy);

        packets_in[packets_in_count++] = new Packet(opcode, loc, origin, rebroadcast_origin);

        // Rebroadcast
        if (bot.type != RobotType.SOLDIER) {
          if (origin.equals(rebroadcast_origin) || bot.distanceSquaredTo(origin) > origin.distanceSquaredTo(rebroadcast_origin)) {
            addMessage(new Packet(opcode, loc, origin, bot.loc));
          }
        }
      }
    }
  }

  /**
   * Compute mac by padding message with the next round's pad.
   */
  private int encode(int msg) {
    return msg ^ Codex.code_by_round[(Clock.getRoundNum() + 1) % Codex.code_by_round.length];
  }

  /**
   * Decode message send last round with this round's pad.
   */
  private int decode(int code) {
    return code ^ Codex.code_by_round[Clock.getRoundNum() % Codex.code_by_round.length];
  }

}
