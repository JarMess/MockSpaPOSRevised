package mockSpaPOS.model;

public class BusinessInfo {
    private static String company;
    private static String address;
    private static String citystatezip;

    public BusinessInfo(){
        company="Test Company";
        address="123 Test St";
        citystatezip="Test City, TC 12345";
    }

    public static String getCompany() {
        return company;
    }

    public static String getAddress() {
        return address;
    }

    public static String getCitystatezip() {
        return citystatezip;
    }
}
