package msgserver.global;


import base.pdu.PduHandler;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/4/14 0014.
 */
public class MsgGlobal {
    public static HashMap<Integer, PduHandler> handlerMap = new HashMap<>();
}
