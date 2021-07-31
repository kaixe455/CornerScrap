package utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class Telegram {

	    public static void sendToTelegram(String chat, String equipoLocal, String equipoVisitante, String liga, String fecha, Integer porcentaje, String lineaGol, String consejo) {
	        String urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s&parse_mode=HTML";
	        //Telegram token
	        String apiToken = "1911960083:AAG3uSc58LoV55MzG-RSATwDeGoaYRhjEZY";
	      
	        //chatId
	        String chatId = chat;
	        String text = "%3Cb%3E"+equipoLocal+" - "+ equipoVisitante + "%3C%2Fb%3E" +"%0A%E2%9A%BD%20"+ liga + " " + fecha
	        		+ "%0A" + "%F0%9F%9A%A9" + "%20" + consejo;
	        urlString = String.format(urlString, apiToken, chatId, text);

	        try {
	            URL url = new URL(urlString);
	            URLConnection conn = url.openConnection();
	            InputStream is = new BufferedInputStream(conn.getInputStream());
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	

}
