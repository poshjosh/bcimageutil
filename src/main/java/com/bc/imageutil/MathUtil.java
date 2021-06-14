package com.bc.imageutil;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.logging.Logger;

/**
 * @author hp
 */
public class MathUtil {
    
    private static final Logger LOG = Logger.getLogger(MathUtil.class.getName());
    
    static MathContext getDefaultContext() {
        return MathContext.DECIMAL32;
    }

    public static BigDecimal multiply(double a, double b) {
        return multiply(getDefaultContext(), a, b);
    }
    
    public static BigDecimal multiply(MathContext context, double a, double b) {
        BigDecimal output = BigDecimal.valueOf(a).multiply(BigDecimal.valueOf(b), context);
        LOG.fine(() -> a + " multiply by " + b + " = " + output);
        return output;
    }

    public static BigDecimal divide(double a, double b) {
        return divide(getDefaultContext(), a, b);
    }
    
    public static BigDecimal divide(MathContext context, double a, double b) {
        BigDecimal output = BigDecimal.valueOf(a).divide(BigDecimal.valueOf(b), context);
        LOG.fine(() -> a + " divide by " + b + " = " + output);
        return output;
    }
}
