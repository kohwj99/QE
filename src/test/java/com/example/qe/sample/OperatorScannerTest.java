package com.example.qe.sample;

import com.example.qe.queryengine.operator.OperatorRegistry;
import com.example.qe.queryengine.operator.OperatorScanner;
import com.example.qe.queryengine.operator.impl.EqualsOperator;
import com.example.qe.queryengine.operator.impl.DayEqualOperator;
import com.example.qe.queryengine.operator.OperatorAnnotation;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class OperatorScannerTest {

    @Test
    void testScanAndRegisterRegistersAnnotatedOperatorsWithCorrectTypes() {
        OperatorRegistry registry = new OperatorRegistry();
        OperatorScanner scanner = new OperatorScanner(registry);

        scanner.scanAndRegister("com.example.qe.queryengine.operator.impl");

        // EqualsOperator checks
        Object equalsOp = registry.getOperator("equals");
        assertNotNull(equalsOp, "EqualsOperator should be registered");
        assertTrue(equalsOp instanceof EqualsOperator);

        OperatorAnnotation equalsAnn = EqualsOperator.class.getAnnotation(OperatorAnnotation.class);
        assertNotNull(equalsAnn, "EqualsOperator should have OperatorAnnotation");
        assertArrayEquals(
                new Class[]{String.class, BigDecimal.class, Boolean.class, LocalDate.class},
                equalsAnn.supportedFieldTypes(),
                "EqualsOperator supportedFieldTypes mismatch"
        );
        assertArrayEquals(
                new Class[]{String.class, BigDecimal.class, Boolean.class, LocalDate.class},
                equalsAnn.supportedValueTypes(),
                "EqualsOperator supportedValueTypes mismatch"
        );

        // DayEqualOperator checks
        Object dayEqualOp = registry.getOperator("dayEqual");
        assertNotNull(dayEqualOp, "DayEqualOperator should be registered");
        assertTrue(dayEqualOp instanceof DayEqualOperator);

        OperatorAnnotation dayEqualAnn = DayEqualOperator.class.getAnnotation(OperatorAnnotation.class);
        assertNotNull(dayEqualAnn, "DayEqualOperator should have OperatorAnnotation");
        assertArrayEquals(
                new Class[]{LocalDate.class},
                dayEqualAnn.supportedFieldTypes(),
                "DayEqualOperator supportedFieldTypes mismatch"
        );
        assertArrayEquals(
                new Class[]{BigDecimal.class},
                dayEqualAnn.supportedValueTypes(),
                "DayEqualOperator supportedValueTypes mismatch"
        );
    }
}