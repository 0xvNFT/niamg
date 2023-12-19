package com.play.common.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BigDecimalUtil {
    protected static Logger logger = LoggerFactory.getLogger(BigDecimalUtil.class);

    public static final BigDecimal TEN = new BigDecimal("10");
    public static final BigDecimal FIFTY = new BigDecimal("50");
    public static final BigDecimal SEVENTY = new BigDecimal("70");
    public static final BigDecimal EIGHTY = new BigDecimal("80");
    public static final BigDecimal HUNDRED = new BigDecimal("100");
    public static final BigDecimal THOUSAND = new BigDecimal("1000");

    public static BigDecimal addAll(BigDecimal... bds) {
        BigDecimal all = BigDecimal.ZERO;
        if (bds != null) {
            for (BigDecimal bd : bds) {
                if (bd != null) {
                    all = all.add(bd);
                }
            }
        }
        return all;
    }

    public static BigDecimal subtract(BigDecimal minuend, BigDecimal... meiosises) {
        if (minuend == null) {
            return null;
        }
        BigDecimal meiosis = BigDecimal.ZERO;
        if (meiosises != null) {
            for (BigDecimal bd : meiosises) {
                if (bd != null) {
                    meiosis = meiosis.add(bd);
                }
            }
        }
        return minuend.subtract(meiosis);
    }

    public static BigDecimal multiply(BigDecimal... as) {
        BigDecimal r = BigDecimal.ONE;
        if (as != null && as.length > 0) {
            for (BigDecimal a : as) {
                if (a != null) {
                    r = r.multiply(a);
                } else {
                    r = BigDecimal.ZERO;
                }
            }
        }
        return r;
    }

    public static BigDecimal multiplyInts(BigDecimal a, Integer... bs) {
        BigDecimal r = BigDecimal.ONE;
        if (a != null) {
            r = a;
        }
        if (bs != null && bs.length > 0) {
            for (Integer b : bs) {
                if (b != null) {
                    r = r.multiply(new BigDecimal(b));
                } else {
                    r = BigDecimal.ZERO;
                }
            }
        }
        return r;
    }

    public static BigDecimal multiplyB2Ints(BigDecimal a, BigDecimal b, Integer... cs) {
        BigDecimal r = BigDecimal.ONE;
        if (a != null) {
            r = a;
        }
        if (b != null) {
            r = r.multiply(b);
        }
        if (cs != null && cs.length > 0) {
            for (Integer c : cs) {
                if (c != null) {
                    r = r.multiply(new BigDecimal(c));
                } else {
                    r = BigDecimal.ZERO;
                }
            }
        }
        return r;
    }

    public static BigDecimal divide(BigDecimal a, BigDecimal b) {
        return divide(a, b, 2);
    }

    public static BigDecimal divide(BigDecimal a, BigDecimal b, int scale) {
        BigDecimal p = BigDecimal.ZERO;
        if (a != null && b != null) {
            if (b.compareTo(BigDecimal.ZERO) == 0) {
                // throw new RuntimeException("被除数不能为0");
                return p;
            }
            p = a.divide(b, scale, RoundingMode.UP);
        }
        return p;
    }

    public static BigDecimal divide(BigDecimal a, Integer b) {
        return divide(a, b, 2);
    }

    public static BigDecimal divide(BigDecimal a, Integer b, int scale) {
        return divide(a, b, scale, RoundingMode.UP);
    }

    public static BigDecimal divide(BigDecimal a, Integer b, int scale, RoundingMode mode) {
        BigDecimal p = BigDecimal.ZERO;
        if (a != null && b != null) {
            if (!b.equals(0)) {
                p = a.divide(new BigDecimal(b), scale, mode);
            }
        }
        return p;
    }

    public static BigDecimal abs(BigDecimal a) {
        if (a != null) {
            return a.abs();
        }
        return null;
    }

    public static BigDecimal absOr0(BigDecimal val) {
        if (val == null) {
            return BigDecimal.ZERO;
        }
        return val.abs();
    }

    public static BigDecimal toBigDecimal(String str) {
        if (StringUtils.isEmpty(str))
            return null;
        try {
            return new BigDecimal(str);
        } catch (Exception e) {

        }
        return null;
    }

    public static BigDecimal toBigDecimalDefaultZero(String str) {
        if (StringUtils.isEmpty(str))
            return BigDecimal.ZERO;
        try {
            return new BigDecimal(str);
        } catch (Exception e) {
        }
        return BigDecimal.ZERO;
    }

    public static BigDecimal formatValue(BigDecimal bigDecimal) {
        try {
            if (Objects.isNull(bigDecimal)) {
                return null;
            }
            int scale = bigDecimal.scale();
            if (scale <= 3) {
                return bigDecimal;
            }
            return bigDecimal.setScale(2, BigDecimal.ROUND_DOWN);
        } catch (Exception e) {
            logger.error("formatValue parse error===>", e.getMessage());
            return BigDecimal.ZERO;
        }
    }

    public static long roundDown(BigDecimal bigDecimal) {
        if (bigDecimal.compareTo(BigDecimal.ZERO)==0){
            return bigDecimal.longValue();
        }
        // 向下取整
        return bigDecimal.setScale( 0, BigDecimal.ROUND_DOWN).longValue();

    }

}
