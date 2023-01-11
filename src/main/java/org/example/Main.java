package org.example;

public class Main {

    public static void main(String[] args) {
        int nPages = 2;
        String urlWithoutOffset = "https://www.booking.com/searchresults.es.html?label=gran-canaria-OUG" +
                "RijUMAc8659bH5DeYlwS410722625015%3Apl%3Ata%3Ap1%3Ap2%3Aac%3Aap%3Aneg%3Afi%3Atikwd-2877496057%3Alp1005463%3A" +
                "li%3Adec%3Adm%3Appccp%3DUmFuZG9tSVYkc2RlIyh9YTiRJUvwM0AZDoPI6XBbFtM&sid=5cd4ae3e13df6db0916e6dc06398f874&aid" +
                "=1610834&region=754&offset=";

        Controller controller = new Controller();
        controller.run(nPages, urlWithoutOffset);
    }
}
