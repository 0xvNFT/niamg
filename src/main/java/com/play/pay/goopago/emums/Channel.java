package com.play.pay.goopago.emums;

import com.play.pay.goopago.result.AgentpayResult;
import com.play.pay.goopago.result.CollectionResult;
import com.play.pay.goopago.result.PaymeCollectionResult;

/**
 * @author zzn
 */
public enum Channel {
    /** 巴西相关渠道 */
    PAYME("br_payme",
            PaymeCollectionResult.class,
            AgentpayResult.class
    ),
    BR2("br_br2",
            CollectionResult.class,
            AgentpayResult.class
    ),
    IUGU("br_iugu",
            CollectionResult.class,
            AgentpayResult.class
    ),
    BR3("br_br3",
            CollectionResult.class,
            AgentpayResult.class
    ),
    TRO("br_tro",
            CollectionResult.class,
            AgentpayResult.class
    ),
    TRFA("br_trfa",
            CollectionResult.class,
            AgentpayResult.class
    ),
    SKB("br_skb",
            CollectionResult.class,
            AgentpayResult.class
    ),
    IAU("br_iau",
            CollectionResult.class,
            AgentpayResult.class
    ),
    AUTO("br_auto",
            CollectionResult.class,
            AgentpayResult.class
    );

     String tmId;
     Class collectionResultClass;
     Class agentResultClass;

    Channel( String tmId, Class collectionResultClass, Class agentResultClass) {
    	this.tmId = tmId;
    	this.collectionResultClass = collectionResultClass;
    	this.agentResultClass = agentResultClass;
    }
}
