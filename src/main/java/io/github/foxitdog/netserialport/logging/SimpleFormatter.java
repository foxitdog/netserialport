package io.github.foxitdog.netserialport.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import io.github.foxitdog.netserialport.utils.TimeFormatUtils;


/**
 * 格式化输出
 * 格式：[type] [date] [logger] [message] throw\n
 * 例：[info] [2020-10-10 10:10:10:101] [com.sudy.xxx.xxx.xx] [hhhhhhhhhhhh] asdfasdfsdfas
 * @author foxitdog
 */
public class SimpleFormatter extends Formatter {

    @Override
    public String format(LogRecord record) {
        String dateTime = TimeFormatUtils.yMdHms_.format(record.getMillis());
        String throwable = "";
        if (record.getThrown() != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            pw.println();
            record.getThrown().printStackTrace(pw);
            pw.close();
            throwable = sw.toString();
        }
        return "[" + record.getLevel().getName() + "] [" + record.getLoggerName() + "] [" + dateTime + "] ["
                + record.getMessage() + "] " + throwable + "\n";
    }

}
