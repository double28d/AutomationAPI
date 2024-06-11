package objectData.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

@Getter
@Setter
public class RequestBook1 {

    private String userID;
    private String collectionOfIsbns;
    private String isbn;

    public RequestBook1 (HashMap <String, String> testData) {
        populateObject(testData);

    }

    private void populateObject (HashMap <String, String> testData) {
        for (String key: testData.keySet()){
            switch (key){
                case "userID":
                    setUserID(getPreparedValue(testData.get(key)));
                    break;
                case "collectionOfIsbns":
                    setCollectionOfIsbns(testData.get(key));
                    break;
                case "isbn":
                    setIsbn(testData.get(key));
                    break;
            }
        }
    }

    private String getPreparedValue (String value){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return value + dtf.format(now);
    }

}
