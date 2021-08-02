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
	        String text =Constantes.BOLA_MUNDO + "%20" + "%3Cb%3E" + escaparSignos(equipoLocal)+" vs "+ escaparSignos(equipoVisitante) + "%3C%2Fb%3E" +"%0A%E2%9A%BD%20"+ escaparSignos(liga) + " " + fecha
	        		+ "%0A" + Constantes.BANDERIN + "%20" + escaparSignos(consejo);
	        urlString = String.format(urlString, apiToken, chatId, text);

	        try {
	            URL url = new URL(urlString);
	            URLConnection conn = url.openConnection();
	            InputStream is = new BufferedInputStream(conn.getInputStream());
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    
	    public static void sendToTelegramHA(String chat, String equipoLocal, String equipoVisitante, String liga, String fecha, String consejo) {
	        String urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s&parse_mode=HTML";
	        //Telegram token
	        String apiToken = "1911960083:AAG3uSc58LoV55MzG-RSATwDeGoaYRhjEZY";
	      
	        //chatId
	        String chatId = chat;
	        String text = Constantes.BOLA_MUNDO + "%20" + "%3Cb%3E" + escaparSignos(equipoLocal) + " vs "+ escaparSignos(equipoVisitante) + "%3C%2Fb%3E" +"%0A%E2%9A%BD%20"+ escaparSignos(liga) + " " + fecha
	        		+ "%0A" + Constantes.HANDICAP_LOGO + "%20" + escaparSignos(consejo);
	        urlString = String.format(urlString, apiToken, chatId, text);

	        try {
	            URL url = new URL(urlString);
	            URLConnection conn = url.openConnection();
	            InputStream is = new BufferedInputStream(conn.getInputStream());
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    
	    @SuppressWarnings("unused")
		public static void sendToTelegramGoals(String chat, String equipoLocal, String equipoVisitante, String liga, String fecha, Integer porcentaje, String lineaGol, String consejo) {
	        String urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s&parse_mode=HTML";
	        //Telegram token
	        String apiToken = "1911960083:AAG3uSc58LoV55MzG-RSATwDeGoaYRhjEZY";
	      
	        //chatId
	        String chatId = chat;
	        String text = Constantes.BOLA_MUNDO + "%20" + "%3Cb%3E" + escaparSignos(equipoLocal) + " vs "+ escaparSignos(equipoVisitante) + "%3C%2Fb%3E"
	        		+"%0A" + Constantes.BALON_FUTBOL + "%20" +escaparSignos(liga) + " " + fecha
	        		+ "%0A" + Constantes.BALON_FUTBOL + "%20" + escaparSignos(consejo);
	        urlString = String.format(urlString, apiToken, chatId, text);

	        try {
	            URL url = new URL(urlString);
	            URLConnection conn = url.openConnection();
	            InputStream is = new BufferedInputStream(conn.getInputStream());
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    
	    public static boolean isMandarGoalOver(int porcentajeSuma, String lineaGol)  {
	    	if (porcentajeSuma >= 100 && Double.parseDouble(lineaGol) <= 2.5) {
	    		return true;
	    	}else {
	    		return false;
	    	}
	    	
	    	
	    }
	    
	    public static boolean isMandarHA(String consejo, String equipoLocal, boolean esLocal)  {
	    	boolean contieneSignoMas = consejo.contains("+");
	    	
	    	if (contieneSignoMas && esLocal) {
	    		return true;
	    	}else {
	    		return false;
	    	}
	    	
	    	
	    }
	    
	    public static boolean consejoEsEquipoLocal (String consejo, String equipoLocal) {
	    	String[] consejoSeparado = consejo.split("\\+");
	    	if(consejoSeparado[0].trim().equalsIgnoreCase(equipoLocal.trim())) {
	    		return true;
	    	}else {
	    		return false;
	    	}
	    }
	    
	    public static String escaparSignos (String palabra) {
	    	
	    	String palabraEscapada = palabra;
	 
	    	if(palabra.contains("+")) {
	    		palabraEscapada = palabra.replace("+", "%2B");
	    	}
	    	if (palabra.contains("-")) {
	    		palabraEscapada = palabra.replace("-", "//-");
	    	}
	    	
	    	return palabraEscapada;
	    }
	

}
