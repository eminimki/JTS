package eminimki.com.JTS.Common.Utilities;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;

@NoArgsConstructor
@Data
public class CommonUtils {

    // Format the current date and time as dd/MM/yyyy - HH:mm:ss
    public static String getCurrentDateTimeFormatted() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
        Date now = new Date();
        return dateFormat.format(now);
    }
}
