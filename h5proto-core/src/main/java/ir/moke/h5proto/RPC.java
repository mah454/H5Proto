package ir.moke.h5proto;

import java.util.Arrays;
import java.util.List;

public enum RPC {
    PUBLIC_KEY(0x00002),
    AUTH_CELLPHONE_NUMBER(0x00003),
    ACTIVATION_KEY(0x00004),
    INVALID_ACTIVATION_KEY(0x00005),
    MESSAGE_TEXT(0x00006),
    ;

    private final int code;

    RPC(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static List<RPC> list() {
        return Arrays.stream(RPC.class.getEnumConstants()).toList();
    }

    public static RPC getValue(String codeStr) {
        int code = Integer.parseInt(codeStr);
        return list().stream().filter(item -> item.code == code).findFirst().orElse(null);
    }
}
