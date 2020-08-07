package io.CarPortal.Utilities;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.*;

public interface CommonUtilities {

    public static <T> int get_RandomIndexNumber_FromList(List<T> T) {
        return ThreadLocalRandom.current().nextInt(T.size()) % T.size();
    }

    public static Supplier<Integer> getRandomData = () -> (int) Math.round(Math.random() * 99999);

    public static UnaryOperator<LocalDateTime> getRandomLocalDateTimeShortRange = localDateTime -> localDateTime.plusDays(new Random().nextInt((10 - 5 + 1) )+ 5);

    public static Function<Exception,String> stracktraceToString=e->{
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            return sw.toString();
    };

    public static Predicate<Integer> verifyStatusCode=intStatus -> {
    	return (intStatus==201)?true:(intStatus==200)?true
    			:(intStatus==400)?false:(intStatus==401)?false:(intStatus==404)?false:(intStatus==406)?false
    					:(intStatus==409)?false:(intStatus==500)?false:false;
    };

    public static BiFunction<Integer,Integer,Boolean> greaterThan=(input1, input2)-> input1>input2;

    public static Function<String, LocalDateTime> getLocalDateTimeFromDateString = dateString -> {
        LocalDateTime dateTime = null;
        try {
            dateTime = LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        } catch (Exception e) {
            e.getMessage();
        }
        return dateTime;
    };

    public static BinaryOperator<Double> getRandomDoubleWithinRange = (minLimit, maxLimit) -> minLimit + new Random().nextDouble() * (maxLimit - minLimit);
}
