package newsaggregator.controller.dashboard.trend;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import newsaggregator.controller.dashboard.trend.timedisplayer.HalfYearRange;
import newsaggregator.controller.dashboard.trend.timedisplayer.TimeDisplay;
import org.json.simple.JSONArray;
import org.json.simple.parser.*;

import newsaggregator.controller.dashboard.trend.pairdata.*;

public class TrendFinder {
    private String outputFile;
    private DataExtract extractor;
    private TimeDisplay timeDisplayer;

    public TrendFinder(String outputFile) throws FileNotFoundException, IOException, ParseException {
        //Khởi tạo
        this.outputFile = outputFile;
        Object object = new JSONParser().parse(new FileReader(this.outputFile));
        JSONArray jsonArray = (JSONArray) object;  //Lấy dữ liệu từ file Json
        this.extractor = new DataExtract(jsonArray);
        TagRecognition.tagScrapping(jsonArray);
    }

    public PairArray trendOverTime(String locateWord, int yearSpan) {
        PairArray dateList = this.extractor.dateExtract(locateWord);
        timeDisplayer = new HalfYearRange(dateList);
        dateList = timeDisplayer.timeRangeGrouping(yearSpan);
        for (int i = 0; i < dateList.size(); i++) {
            //Chỉnh lại format từ năm-tháng-ngày sang năm/tháng/ngày
            String newDate = dateList.getProperty(i).replace('-', '/');
            newDate = newDate.replace('_', '-');
            dateList.setProperty(i, newDate);
        }
        return dateList;
    }


    public PairArray extractedWeb() throws FileNotFoundException, IOException, ParseException {
        PairArray listOfWeb = this.extractor.mainWebListing();
        listOfWeb.sortValue();
        double total = 0;
        for (int i = 0; i < listOfWeb.size(); i++) {
            //lấy tổng số lượng web
            total += listOfWeb.getValue(i);
        }
        PairArray webPrecentList = new PairArray();
        for (int i = 0; i < listOfWeb.size(); i++) {
            //chia dần ra lấy phần trăm
            double precentValue = listOfWeb.getValue(i) / total * 100;
            precentValue = Math.round(precentValue * 100) / 100.0;
            webPrecentList.add(listOfWeb.getProperty(i), precentValue);
        }
        return webPrecentList;
    }

    public PairArray findMostTrending(int findNumber) throws FileNotFoundException, IOException, ParseException {
        PairArray trendList = extractor.extractTagTrend();
        PairArray mostTrend = new PairArray();
        //cắt dãy con không cần thiết tùy vào findNumber
        //trường hợp là số lượng từ tìm bằng độ sâu của cây vung đống
        double maxIndex = Math.pow(2, findNumber);
        while (trendList.size() < maxIndex) {
            maxIndex = maxIndex / 2;
        }
        mostTrend = trendList.subList(0, (int) maxIndex);
        mostTrend.sortValue();
        if (findNumber > mostTrend.size()) {
            findNumber = mostTrend.size();
        }
        mostTrend = mostTrend.subList(0, findNumber); //cắt tỉa nốt để lấy các từ cần thiết
        return mostTrend;
    }
}
