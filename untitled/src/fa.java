import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class fa {
        public static void main(String[] args) {
                String startDate = "2018-02-16";
                String endDate = "2019-12-18";
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                List<String> months = new ArrayList<>();
                try {
                        Date startDate1 = sdf.parse(startDate);
                        Date endDate1 = sdf.parse(endDate);
                        Calendar calendar1 = Calendar.getInstance();
                        Calendar calendar2 = Calendar.getInstance();
                        calendar1.setTime(startDate1);
                        calendar2.setTime(endDate1);
                        months.add(sdf.format(startDate1));
                        while (calendar1.compareTo(calendar2)<0){
                                calendar1.add(Calendar.MONTH,1);
                                months.add(sdf.format(calendar1.getTime()));
                        }
                        System.out.println(months);
                } catch (Exception e) {
                        e.printStackTrace();
                }

        }
}
