import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import model.entity.PartidoPdf;
import model.entity.PartidosHoy;
import model.entity.PartidosResultado;
import utils.EnvioEmail;
import utils.ScrapeUtils;
import utils.Telegram;
import utils.Constantes;


public class Main {
	private static JLabel statusLabel; 
	private static JProgressBar progressBar;
    private static JFrame frame;
    private static JButton btnSubmit;
    final static String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36"; 
    final static String LOGIN_FORM_URL = "https://www.totalcorner.com/user/login";
    final static String USERNAME = "Morigeri";  
    final static String PASSWORD = "Morigeri80";
    List<String> listaPartidosFalladosTelegram = new ArrayList<String>();
    static String [] listAgent  = {"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.86 Safari/537.36",
                            "Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.86 Safari/537.36",
                            "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.86 Safari/537.36",
                            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.86 Safari/537.36",
                            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.86 Safari/537.36",
                            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.86 Safari/537.36",
                             "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.86 Safari/537.36",
                             "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.86 Safari/537.36",
                             "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.86 Safari/537.36",
                             "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.86 Safari/537.36",
                             "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.78 Safari/537.36",
                             "Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.78 Safari/537.36",
                             "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.78 Safari/537.36",
                             "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.78 Safari/537.36",
                             "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.78 Safari/537.36",
                             "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.78 Safari/537.36",
                             "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.78 Safari/537.36",
                             "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.78 Safari/537.36",
                             "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.78 Safari/537.36",
                             "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.78 Safari/537.36",
                             "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.79 Safari/537.36",
                             "Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.79 Safari/537.36",
                             "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.79 Safari/537.36",
                             "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.79 Safari/537.36",
                             "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.79 Safari/537.36",
                             "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.79 Safari/537.36",
                             "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.79 Safari/537.36",
                             "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.79 Safari/537.36",
                             "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.79 Safari/537.36",
                             "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.79 Safari/537.36"};
	public static void main(String[] args) {
		frame = new JFrame("TopCornerSCRAP V4.3 by xKaixe");
        frame.setBounds(100, 100, 400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        btnSubmit = new JButton("Generar consejos");
        btnSubmit.setBounds(120, 80, 150, 23); 
        btnSubmit.setEnabled(true);
		progressBar = new JProgressBar(0, 1000);
		progressBar.setBounds(90, 40, 200, 30);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		statusLabel = new JLabel();
		statusLabel.setBounds(90, 5, 200, 30);
		statusLabel.setVisible(true);
	    frame.getContentPane().add(progressBar);
	    frame.getContentPane().add(statusLabel);
        btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent arg0) {
					progressBar.setVisible(true);
					statusLabel.setVisible(true);
					btnSubmit.setEnabled(false);
					statusLabel.setText("Comenzando busqueda de partidos");
					startThread();
				
			}
        });
        frame.getContentPane().add(btnSubmit);
        frame.getContentPane().setLayout(null);
        frame.setVisible(true);
		 
		
	}

	public static PartidoPdf iniciarBot (PartidosResultado partido) {
			// genero la lista de ids de los partidos que tengo que consultar.
		List<Object> listaElementosPdf = new ArrayList<Object>();
//		ByteArrayOutputStream outputStreamEmail2 = new ByteArrayOutputStream();
//        byte[] bytesEmail = outputStreamEmail2.toByteArray();
//        EnvioEmail.enviar("CONSEJOS", bytesEmail);
				try {
						String partidoH = partido.getH();
						String partidoA = partido.getA();
						String liga = partido.getL();
						if(!esEsports(partidoH) && !esEsports(liga)) {
							String url = "https://totalcorner.com/match/corner-stats/"+partido.getId();
					        Document document = null;
					        Random r = new Random();
					        String userAgent = listAgent[r.nextInt(listAgent.length)];
					        Connection con = Jsoup.connect(url).header("Accept-Encoding", "gzip, deflate").userAgent(userAgent).maxBodySize(0).timeout(60000).ignoreHttpErrors(true);
					        Connection.Response resp = con.execute();
					        if(resp.statusCode() == 429) {
								int segundosEsperar = Integer.parseInt(resp.header("Retry-After"));
								statusLabel.setText("Seguimos en: " + segundosEsperar + " segundos");
								System.out.println("TotalCorner nos ha echado - Seguimos en: " + segundosEsperar + " segundos");
								Thread.sleep(segundosEsperar*1000);
								userAgent = listAgent[r.nextInt(listAgent.length)];
								con = Jsoup.connect(url).header("Accept-Encoding", "gzip, deflate").userAgent(userAgent).maxBodySize(0).timeout(60000).ignoreHttpErrors(true);
								resp = con.execute();
				        	}	
					        if (resp.statusCode() == 200) {
					        	Thread.sleep(5000);
						        document = con.get();
						        String [] datosEquipoLocal = new String[9];
						        String [] datosEquipoVisitante = new String[9];
						        Elements datos = document.select("div.match-facts-history");
						        for(int i = 0; i < datos.size();  i++) {
							        Element equipo =  datos.get(i);
							        // obtengo los facts de cada equipo
							       
							        if(i==0) {
							        	  datosEquipoLocal = obtenerFactsEquipo(equipo);
							        	  System.out.println(datosEquipoLocal);
							         }
							        if(i==1) {
							        	  datosEquipoVisitante = obtenerFactsEquipo(equipo);
							        	  System.out.println(datosEquipoVisitante);
							         }
						         }
						        // datos iniciales
						        LineSeparator ls = new LineSeparator();
						        listaElementosPdf.add(new Chunk(ls));
					            //documentoPdf.add(new Chunk(ls));
						        listaElementosPdf.add(Chunk.NEWLINE);
					           // documentoPdf.add(Chunk.NEWLINE);
					            // inserto enlace
								Phrase phrase = new Phrase();
					            phrase.add("Enlace: ");
					            Chunk chunk = new Chunk(url);
					            chunk.setAnchor(url);
					            phrase.add(chunk);
					            //documentoPdf.add(phrase);
					            listaElementosPdf.add(phrase);
					            // inserto equipos y liga
					            Paragraph phraseEquipos = new Paragraph();
					            phraseEquipos.add(Chunk.NEWLINE);
					            phraseEquipos.add(partidoH+" - "+partidoA + " ----- "+liga);
					            // inserto partidos de estadisticas
					            Paragraph phrasePartidosJugados = new Paragraph();
					            phrasePartidosJugados.add(Chunk.NEWLINE);
					            phrasePartidosJugados.add("Número de partidos: "+datosEquipoLocal[5]+" - "+datosEquipoVisitante[5] + " Goles marcados: " + datosEquipoLocal[6] + " - " + datosEquipoVisitante[6] + " Goles encajados: "+ datosEquipoLocal[7] + " - " + datosEquipoVisitante[7]);
					            // inserto hora Y dia del partido
					            Paragraph phraseHora = new Paragraph();
					            phraseHora.add(Chunk.NEWLINE);
					            String fechaCompleta = partido.getStart();
					            String [] hora = fechaCompleta.split(" ");
					            phraseHora.add("Hora inicio: "+hora[1]);
					            listaElementosPdf.add(phraseEquipos);
					            listaElementosPdf.add(phrasePartidosJugados);
					            listaElementosPdf.add(phraseHora);
					            //documentoPdf.add(phraseEquipos);
					            //documentoPdf.add(phrasePartidosJugados);
					            //documentoPdf.add(phraseHora);
						        // handicap asiatico
						        String[] handicapAsiaticoJson = partido.getP_asian();
						        String handicapAsiaticoFinal = calcularHandicapAsiatico(handicapAsiaticoJson);
						       // obtengo las cuotas
						        String[] apuestas = partido.getP_odds();
						       // obtengo linea asiatica de gol
						       Elements divPredicciones = document.select("div.match-facts-pred");
						       String predicciones[] = divPredicciones.text().split(" ");
						       String prediccionesComa[] = divPredicciones.text().split(",");
						       int indiceCoger = prediccionesComa.length - 2;
						       String lineaGolAsiatica = calcularLineaGolAsiatica(partido,predicciones);
						       // para calcular diferencia de goles multiplico por 10.
						       Double lineaDeGolAsiaticaInt = null;
						       Integer lineaGolAsiaticaIntReal = null;
						       try {
							       	lineaDeGolAsiaticaInt = Double.valueOf(lineaGolAsiatica)*10;
									lineaGolAsiaticaIntReal = lineaDeGolAsiaticaInt.intValue();
						       }catch (Exception e) {
						    	   	System.out.println("No hay linea de gol - Partido Id: "+ partido.getId()+ "No se puede parsear: "+lineaGolAsiatica +" o "+lineaDeGolAsiaticaInt+ "excepcion: "+e.getMessage());
						       }
						       System.out.println(divPredicciones.text());
						       //equipoFavorito
						       boolean favoritoLocal = favoritoLocal(apuestas);
						       //Calculo corner over
						       String[] cornerOver = partido.getP_corner();
						       boolean tieneCorner= esPartidoCorner(cornerOver);
						    	   // calculo corner si tiene corner
						       	if(tieneCorner) {
						       		//Elements tablas = document2.select("table.home_stats_table"); 
						       		//Elements barras = document.select("span.meter");
						       		Elements cornerOverPorciento;
						       		Elements cornerOverEmpateLocal; 
						       		Elements cornerOverPorcientoVisitante;
						       		Elements cornerOverEmpateVisitante;
						       		Elements cuotaCornerOver;
						       		Elements cuotaCornerUnder;
						       		String cornerOverCuota;
					       			String cornerUnderCuota;
						       		// si no tiene gol no aparece la barra de goles y los corners suben una posición
						       		if(partido.getP_goal()[0].equals("")) {
//							       		cornerOverPorciento = barras.get(4);
//							       		cornerOverEmpateLocal = barras.get(5);
//							       		cornerOverPorcientoVisitante = barras.get(6);
//							       		cornerOverEmpateVisitante = barras.get(7);
						       			cornerOverPorciento = document.select("#match_facts_container > div.col-xs-12.no-left-padding.no-right-padding > div > div:nth-child(5) > div:nth-child(1) > div > div:nth-child(3) > div > span:nth-child(1)");
						       			cornerOverEmpateLocal = document.select("#match_facts_container > div.col-xs-12.no-left-padding.no-right-padding > div > div:nth-child(5) > div:nth-child(1) > div > div:nth-child(3) > div > span.meter.draw");
						       			cornerOverPorcientoVisitante = document.select("#match_facts_container > div.col-xs-12.no-left-padding.no-right-padding > div > div:nth-child(5) > div:nth-child(2) > div > div:nth-child(3) > div > span:nth-child(1)");
						       			cornerOverEmpateVisitante = document.select("#match_facts_container > div.col-xs-12.no-left-padding.no-right-padding > div > div:nth-child(5) > div:nth-child(2) > div > div:nth-child(3) > div > span.meter.draw");
						       			cuotaCornerOver = document.select(Constantes.xPATH_CUOTA_CORNER_OVER);
						       			cornerOverCuota = cuotaCornerOver.attr("data-odds");
						       			cuotaCornerUnder = document.select(Constantes.xPATH_CUOTA_CORNER_UNDER);
						       			cornerUnderCuota = cuotaCornerUnder.attr("data-odds");
						       		}else {
//							       		cornerOverPorciento = barras.get(8);
//								       	cornerOverEmpateLocal = barras.get(9);
//								       	cornerOverPorcientoVisitante = barras.get(10);
//								       	cornerOverEmpateVisitante = barras.get(11);
						       			cornerOverPorciento = document.select("#match_facts_container > div.col-xs-12.no-left-padding.no-right-padding > div > div:nth-child(5) > div:nth-child(1) > div > div:nth-child(3) > div > span:nth-child(1)");
						       			cornerOverEmpateLocal = document.select("#match_facts_container > div.col-xs-12.no-left-padding.no-right-padding > div > div:nth-child(5) > div:nth-child(1) > div > div:nth-child(3) > div > span.meter.draw");
						       			cornerOverPorcientoVisitante = document.select("#match_facts_container > div.col-xs-12.no-left-padding.no-right-padding > div > div:nth-child(5) > div:nth-child(2) > div > div:nth-child(3) > div > span:nth-child(1)");
						       			cornerOverEmpateVisitante = document.select("#match_facts_container > div.col-xs-12.no-left-padding.no-right-padding > div > div:nth-child(5) > div:nth-child(2) > div > div:nth-child(3) > div > span.meter.draw");
						       			cuotaCornerOver = document.select(Constantes.xPATH_CUOTA_CORNER_OVER);
						       			cornerOverCuota = cuotaCornerOver.attr("data-odds");
						       			cuotaCornerUnder = document.select(Constantes.xPATH_CUOTA_CORNER_UNDER);
						       			cornerUnderCuota = cuotaCornerUnder.attr("data-odds");
						       		
						       		}
						       		// calculo barra azul
						       		String porcentajeLocal = limpiarPorcentaje(cornerOverPorciento);
						       		String porcentajeVisitante = limpiarPorcentaje(cornerOverPorcientoVisitante);
						       		String porcentajeEmpateLocal = limpiarPorcentaje(cornerOverEmpateLocal);
						       		String porcentajeEmpateVisitante = limpiarPorcentaje(cornerOverEmpateVisitante);
						       		// calculo la barra amarilla
						       		String porcentajeAmarilloLocal = calcularBarraAmarilla(porcentajeEmpateLocal, porcentajeLocal);
						       		String porcentajeAmarilloVisitante = calcularBarraAmarilla(porcentajeEmpateVisitante, porcentajeVisitante);
						       		// calculo consejos
						       		// formateo decimales
						       		DecimalFormat numberFormat = new DecimalFormat("#.##");
						       		//consejo over
						       		boolean apuestaCornerOver = consejoCorner(porcentajeLocal, porcentajeVisitante,divPredicciones);
						       		if(apuestaCornerOver) {
						       			Double totalSumaCorners = null;
						       			try {
						       			totalSumaCorners = calcularSumaCorners(partido.getP_corner()[0], url);
						       			}catch (Exception e) {
											System.out.println("No se ha podido calcular la suma de corners "+ e.getMessage());
										}
						       			if(totalSumaCorners != null) {
						       				Integer sumaPorcentajesConsejo = sumarPorcentajesConsejoCorner(porcentajeLocal, porcentajeVisitante);
						       				String consejoObtenido = obtenerConsejo(divPredicciones, "Corner");
						       				Paragraph paragraphLorem = new Paragraph();
						       				paragraphLorem.add(Chunk.NEWLINE);
						       				paragraphLorem.add("CornerOver: "+ sumaPorcentajesConsejo +"% / * "+ numberFormat.format(totalSumaCorners)+ " * / Linea Asiatica: "+partido.getP_corner()[0] + " / Consejo: "+ consejoObtenido);
							            	listaElementosPdf.add(paragraphLorem);
							            	if(consejoObtenido.contains("over")) {
							            		String partidoEnviado = Telegram.sendToTelegram(Constantes.CHATOVERS, partidoH, partidoA, liga, hora[1], sumaPorcentajesConsejo, partido.getP_corner()[0],consejoObtenido, cornerOverCuota);
							            		if(partidoEnviado == null) {
							            			String urlTelegram = Telegram.getUrlCorners(Constantes.CHATOVERS, partidoH, partidoA, liga, hora[1], sumaPorcentajesConsejo, partido.getP_corner()[0],consejoObtenido, cornerOverCuota);
							            			EnvioEmail.enviarFallidosTelegram(urlTelegram, partido.getId());
							            		}
							            	}
						       			}else {
						       				Integer sumaPorcentajesConsejo = sumarPorcentajesConsejoCorner(porcentajeLocal, porcentajeVisitante);
						       				String consejoObtenido = obtenerConsejo(divPredicciones, "Corner");
						       				Paragraph paragraphLorem = new Paragraph();
						       				paragraphLorem.add(Chunk.NEWLINE);
								            paragraphLorem.add("CornerOver: "+ sumaPorcentajesConsejo +"%"+ " / Linea Asiatica: "+partido.getP_corner()[0] + " / Consejo: "+obtenerConsejo(divPredicciones, "Corner"));
								            listaElementosPdf.add(paragraphLorem);
								            if(consejoObtenido.contains("over")) {
								            	String partidoEnviado = Telegram.sendToTelegram(Constantes.CHATOVERS, partidoH, partidoA, liga, hora[1], sumaPorcentajesConsejo, partido.getP_corner()[0],consejoObtenido, cornerOverCuota);
								            	if(partidoEnviado == null) {
								            		String urlTelegram = Telegram.getUrlCorners(Constantes.CHATOVERS, partidoH, partidoA, liga, hora[1], sumaPorcentajesConsejo, partido.getP_corner()[0],consejoObtenido, cornerOverCuota);
								            		EnvioEmail.enviarFallidosTelegram(urlTelegram, partido.getId());
								            	}
								            }
						       			}
						       		}
						       		// consejo under
						       		boolean apuestaCornerUnder = consejoCornerUnder(porcentajeAmarilloLocal, porcentajeAmarilloVisitante,divPredicciones);
						       		if(apuestaCornerUnder) {
						       			Double totalSumaCorners = null;
						       			try {
							       		totalSumaCorners = calcularSumaCorners(partido.getP_corner()[0], url);
						       			}catch (Exception e) {
											System.out.println("No se ha podido calcular la suma de corners "+ e.getMessage());
										}
						       			if(totalSumaCorners != null) {
						       				Integer sumaPorcentajesConsejo = sumarPorcentajesConsejoCorner(porcentajeAmarilloLocal, porcentajeAmarilloVisitante);
						       				String consejoObtenido = obtenerConsejo(divPredicciones, "Corner");
						       				Paragraph paragraphLorem = new Paragraph();
						       				paragraphLorem.add(Chunk.NEWLINE);
						       				paragraphLorem.add("Corner Under: "+sumarPorcentajesConsejoCorner(porcentajeAmarilloLocal, porcentajeAmarilloVisitante)+"% / * "+ numberFormat.format(totalSumaCorners) + " * / Linea Asiatica: "+partido.getP_corner()[0] + " / Consejo: "+obtenerConsejo(divPredicciones, "Corner"));
						       				listaElementosPdf.add(paragraphLorem);
						       				if(consejoObtenido.contains("under")) {
						       					String partidoEnviado = Telegram.sendToTelegram(Constantes.CHATUNDERS, partidoH, partidoA, liga, hora[1], sumaPorcentajesConsejo, partido.getP_corner()[0],consejoObtenido, cornerUnderCuota);
						       					if(partidoEnviado == null) {
						       						String urlTelegram = Telegram.getUrlCorners(Constantes.CHATUNDERS, partidoH, partidoA, liga, hora[1], sumaPorcentajesConsejo, partido.getP_corner()[0],consejoObtenido, cornerUnderCuota);
						       						EnvioEmail.enviarFallidosTelegram(urlTelegram, partido.getId());
						       					}
						       				}
							            }else {
							            	Integer sumaPorcentajesConsejo = sumarPorcentajesConsejoCorner(porcentajeAmarilloLocal, porcentajeAmarilloVisitante);
						       				String consejoObtenido = obtenerConsejo(divPredicciones, "Corner");
							            	Paragraph paragraphLorem = new Paragraph();
						       				paragraphLorem.add(Chunk.NEWLINE);
						       				paragraphLorem.add("Corner Under: "+sumarPorcentajesConsejoCorner(porcentajeAmarilloLocal, porcentajeAmarilloVisitante)+"%"+ " / Linea Asiatica: "+partido.getP_corner()[0] + " / Consejo: "+obtenerConsejo(divPredicciones, "Corner"));
						       				listaElementosPdf.add(paragraphLorem);
						       				if(consejoObtenido.contains("under")) {
						       					String partidoEnviado = Telegram.sendToTelegram(Constantes.CHATUNDERS, partidoH, partidoA, liga, hora[1], sumaPorcentajesConsejo, partido.getP_corner()[0],consejoObtenido, cornerUnderCuota);
						       					if(partidoEnviado == null) {
						       						String urlTelegram = Telegram.getUrlCorners(Constantes.CHATUNDERS, partidoH, partidoA, liga, hora[1], sumaPorcentajesConsejo, partido.getP_corner()[0],consejoObtenido, cornerUnderCuota);
						       						EnvioEmail.enviarFallidosTelegram(urlTelegram, partido.getId());
						       					}
						       				}
							            }
						       		}
						       	}
						       	// consejo goles
						       if (!lineaGolAsiatica.equals("") && (datosEquipoLocal[0] != null || datosEquipoLocal [1] != null || datosEquipoVisitante [0] != null || datosEquipoVisitante[1] != null)) {
							    	String [] datosBarras = calculoBarrasGoles(document);   
							       	Double consejoGoles = consejoGoles(datosEquipoLocal, datosEquipoVisitante, favoritoLocal, lineaGolAsiatica, datosBarras);
						       		if (consejoGoles != null && consejoGoles > 0){
						       			int porcentajeGolesLocal = Integer.parseInt(datosBarras[0]);
						       			int porcentajeGolesVisitante = Integer.parseInt(datosBarras[1]);
						       			int porcentajeTotal = porcentajeGolesLocal+porcentajeGolesVisitante;
						       			String consejoGoal = obtenerConsejo(divPredicciones, "Goal");
						       			Paragraph paragraphLorem = new Paragraph();
						       			paragraphLorem.add(Chunk.NEWLINE);
							            paragraphLorem.add("Goles Over: "+ porcentajeTotal + "% / Linea de Gol: "+lineaGolAsiatica + " / Consejo: " + consejoGoal);
							            paragraphLorem.add(Chunk.NEWLINE); 
							            //documentoPdf.add(paragraphLorem);
							            listaElementosPdf.add(paragraphLorem);
							            if(Telegram.isMandarGoalOver(porcentajeTotal,lineaGolAsiatica,consejoGoal )) {
							            	String partidoEnviado = Telegram.sendToTelegramGoals(Constantes.CHATGOALSOVER, partidoH, partidoA, liga, hora[1], porcentajeTotal, lineaGolAsiatica, consejoGoal);
							            	if(partidoEnviado == null) {
							            		String urlTelegram = Telegram.getUrlGoals(Constantes.CHATGOALSOVER, partidoH, partidoA, liga, hora[1], porcentajeTotal, lineaGolAsiatica, consejoGoal);
							            		EnvioEmail.enviarFallidosTelegram(urlTelegram, partido.getId());
							            	}
							            }
							            
							            	
							            
						       		}
						       		if (consejoGoles != null && consejoGoles < 0){
						       			int porcentajeGolesLocal = Integer.parseInt(datosBarras[0]);
						       			int porcentajeGolesVisitante = Integer.parseInt(datosBarras[1]);
						       			int porcentajeTotal = porcentajeGolesLocal+porcentajeGolesVisitante;
						       			String consejoGoal = obtenerConsejo(divPredicciones, "Goal");
						       			Paragraph paragraphLorem = new Paragraph();
						       			paragraphLorem.add(Chunk.NEWLINE);
							            paragraphLorem.add("Goles Over: "+ porcentajeTotal +"% /" + " Linea de Gol: "+lineaGolAsiatica + " / Consejo: " + consejoGoal);
							            paragraphLorem.add(Chunk.NEWLINE); 
							            //documentoPdf.add(paragraphLorem);
							            listaElementosPdf.add(paragraphLorem);
							            if(Telegram.isMandarGoalOver(porcentajeTotal,lineaGolAsiatica,consejoGoal)) {
							            	String partidoEnviado = Telegram.sendToTelegramGoals(Constantes.CHATGOALSOVER, partidoH, partidoA, liga, hora[1], porcentajeTotal, lineaGolAsiatica, consejoGoal);
							            	if(partidoEnviado == null) {
							            		String urlTelegram = Telegram.getUrlGoals(Constantes.CHATGOALSOVER, partidoH, partidoA, liga, hora[1], porcentajeTotal, lineaGolAsiatica, consejoGoal);
							            		EnvioEmail.enviarFallidosTelegram(urlTelegram, partido.getId());
							            	}
							            }
						       		}
						       }
						       	// consejo HA Favorito
						       		Double valorConsejoHAFavorito = consejoHAFavoritos(favoritoLocal, apuestas,handicapAsiaticoFinal);
						       		String consejoParentesis = prediccionesComa[indiceCoger];
						            String [] consejoFinal = consejoParentesis.split("\\(");
						            String consejoFinalSeparado [] = null;
						            if(consejoFinal[0].contains("+")) {
						            	consejoFinalSeparado = consejoFinal[0].split("\\+");
						            }
						            if(consejoFinal[0].contains("-")) {
							            consejoFinalSeparado = consejoFinal[0].split("-");
							            }
						            if(consejoFinal[0].contains("0.0")) {
							            consejoFinalSeparado = consejoFinal[0].split("0");
							            }
						       		if(valorConsejoHAFavorito != null) {
						       			Paragraph paragraphLorem = new Paragraph();
						       			paragraphLorem.add(Chunk.NEWLINE);
							            paragraphLorem.add("Cuota Equipo Favorito: " + valorConsejoHAFavorito);
							            paragraphLorem.add(Chunk.NEWLINE);
							            paragraphLorem.add("Consejo: " + consejoFinal[0]);
							            paragraphLorem.add(Chunk.NEWLINE);
							            Integer diferenciaVictorias = null;
							            boolean esLocalConsejo = false;
							            if(consejoFinalSeparado != null) {
							            esLocalConsejo = esEquipoLocal(consejoFinalSeparado[0], partidoH, partidoA);
							            diferenciaVictorias = calcularDiferenciaVictorias(esLocalConsejo,datosEquipoLocal,datosEquipoVisitante);
							            }
							            paragraphLorem.add("Victorias: ("+ diferenciaVictorias + ")  " + datosEquipoLocal[4] + " - " + datosEquipoVisitante[4]);
							           // documentoPdf.add(paragraphLorem);
							            listaElementosPdf.add(paragraphLorem);
							            if(Telegram.isMandarHA(consejoFinal[0],partidoH, esLocalConsejo)) {
							            	String partidoEnviado = Telegram.sendToTelegramHA(Constantes.CHATHADICAP, partidoH, partidoA, liga, hora[1], consejoFinal[0]);
							            	if(partidoEnviado == null) {
							            		String urlTelegram = Telegram.getUrlHA(Constantes.CHATHADICAP, partidoH, partidoA, liga, hora[1], consejoFinal[0]);
							            		EnvioEmail.enviarFallidosTelegram(urlTelegram, partido.getId());
							            	}
							            }
						       		}
						       // consejo HA no Favorito.
						       		
				//		       		Double valorConsejoHANoFavorito = consejoHANoFavoritos(favoritoLocal, apuestas,handicapAsiaticoFinal);
				//		       		if(valorConsejoHANoFavorito != null) {
				//		       			Paragraph paragraphLorem = new Paragraph();
				//		       			paragraphLorem.add(Chunk.NEWLINE);
				//			            paragraphLorem.add("HA No Favoritos: " + valorConsejoHANoFavorito + " cuota equipo favorito");
				//			            documentoPdf.add(paragraphLorem);
				//		       		}
						       // consejo empate
						       		//Double consejoEmpate = consejoEmpate(favoritoLocal, apuestas, handicapAsiaticoFinal, lineaGolAsiatica);
						       		Integer golesMarcadosLocal = pasarInt(datosEquipoLocal[6]);
						       		Integer golesEncajadosLocal = pasarInt(datosEquipoLocal[7]);
						       		Integer golesEncajadosVisitante = pasarInt(datosEquipoVisitante[7]);
						       		if(golesMarcadosLocal != null && golesEncajadosLocal != null && golesEncajadosVisitante != null && golesMarcadosLocal <= 15 && golesEncajadosLocal <= 16 && golesEncajadosVisitante <= 12 && consejoFinal[0] != null && consejoFinal[0].contains("+0.25")  ) {
						       			Paragraph paragraphLorem = new Paragraph();
						       			paragraphLorem.add(Chunk.NEWLINE);
							            paragraphLorem.add("Empate: " + " Linea de gol asiatica " + lineaGolAsiatica + " / Número Empates: " + datosEquipoLocal[8] + " - "+ datosEquipoVisitante[8]);
							            //documentoPdf.add(paragraphLorem);
							            listaElementosPdf.add(paragraphLorem);
						       		}
				
						            Font f = new Font();
						            f.setFamily(FontFamily.COURIER.name());
						            f.setStyle(Font.BOLDITALIC);
						            f.setSize(8);
						            
						           
						            //documentoPdf.add(Chunk.NEWLINE);
						            listaElementosPdf.add(Chunk.NEWLINE);
						            LineSeparator ls2 = new LineSeparator();
						            //documentoPdf.add(new Chunk(ls2));
						            listaElementosPdf.add(new Chunk(ls2));
						            //documentoPdf.add(Chunk.NEWLINE);
						            listaElementosPdf.add(Chunk.NEWLINE);

					        }
					        PartidoPdf partidoPdf = new PartidoPdf();
							partidoPdf.setElementos(listaElementosPdf);
							System.out.println("Partido añadido para imprimir");
							return partidoPdf;
					}else {
						System.out.println("Es partido de esports");
						return null;
					}
				}catch (HttpStatusException h) {
					System.out.println("No se ha podido conectar con el partido - "+h.getMessage());
					return null;
				}catch (Exception j) {
					System.out.println("No se ha podido ver el partido error no controlado - " + j.getMessage() + " "+ j.getStackTrace().toString()+ "idPartido: " + partido.getId());
					return null;
				}
		
		}
	public static String limpiarPorcentaje (Element porcentaje) {
		String porcentajeFinal = porcentaje.attr("style");
   		porcentajeFinal = porcentajeFinal.replace("%", "");
   		porcentajeFinal = porcentajeFinal.replaceAll("\\s","");
   		String [] numeroPorcentaje = porcentajeFinal.split(":");
   		String datoOverCorners = numeroPorcentaje[1];
   		return datoOverCorners;
	}
	public static String limpiarPorcentaje (Elements porcentaje) {
		String porcentajeFinal = porcentaje.attr("style");
   		porcentajeFinal = porcentajeFinal.replace("%", "");
   		porcentajeFinal = porcentajeFinal.replaceAll("\\s","");
   		String [] numeroPorcentaje = porcentajeFinal.split(":");
   		String datoOverCorners = numeroPorcentaje[1];
   		return datoOverCorners;
	}
	public static boolean esEquipoLocal (String equipoConsejo,String equipoLocal,String equipoVisitante) {
		equipoConsejo = equipoConsejo.trim();
		boolean esLocal = false;
		int coincidenciasLocal = 0;
		int coincidenciasVisitante = 0;
		String [] equipoConsejoSplit = equipoConsejo.split(" ");
		String [] equipoLocalSplit = equipoLocal.split(" ");
		String [] equipoVisitanteSplit = equipoVisitante.split(" ");
		for (String parte : equipoConsejoSplit) {
				for(String parteLocal : equipoLocalSplit) {
					if (quitaDiacriticos(parte).equals( quitaDiacriticos(parteLocal))) {
						coincidenciasLocal++;
					}
				}
				for(String parteLocal : equipoVisitanteSplit) {
					if (quitaDiacriticos(parte).equals( quitaDiacriticos(parteLocal))) {
						coincidenciasVisitante++;
					}
				}
		}
		// calculo porcentaje de coincidencias y los comparo
		Double coincidenciaLocalDouble = new Double(coincidenciasLocal);
		Double coincidenciaVisitanteDouble = new Double(coincidenciasVisitante);
		Double totalPalabrasLocal = new Double(equipoLocalSplit.length);
		Double totalPalabrasVisitante = new Double(equipoVisitanteSplit.length);
		Double tasaAciertoLocal = (coincidenciaLocalDouble * 100) / totalPalabrasLocal;
		Double tasaAciertoVisitante = (coincidenciaVisitanteDouble * 100) / totalPalabrasVisitante;
		if(tasaAciertoLocal > tasaAciertoVisitante)
			esLocal = true;
		if(tasaAciertoVisitante > tasaAciertoLocal)
			esLocal = false;
		return esLocal;
	}
	public static String calcularBarraAmarilla (String porcentajeEmpate,String porcentajeAzul) {
		String barraAmarilla = "";
		Integer porcentajeEmpateInt = pasarEntero(porcentajeEmpate);
		Integer porcentajeAzulInt = pasarEntero(porcentajeAzul);
		if(porcentajeEmpate.equals("0")) {
			barraAmarilla = Integer.toString(100 - porcentajeAzulInt);
		}else {
			barraAmarilla = Integer.toString(100 -(porcentajeEmpateInt+porcentajeAzulInt));
		}
		return barraAmarilla;
	}
	public static Integer pasarEntero (String numero) {
		Integer numeroFinal;
			try {
				numeroFinal = Integer.parseInt(numero);
			}catch (NumberFormatException e) {
				System.out.println("No se ha podido parsear a int pasarEntero: "+ e.getMessage());
				return null;
			}
		return numeroFinal;
	}
	public static Double pasarDouble (String numero) {
		Double numeroFinal;
			try {
				numeroFinal = Double.parseDouble(numero);
			}catch (NumberFormatException e) {
				System.out.println("No se ha podido parsear a int pasarEntero: "+ e.getMessage());
				return null;
			}
		return numeroFinal;
	}
	public static boolean consejoCorner (String porcentajeLocal,String porcentajeVisitante, Elements divPred) {
		try {
			Integer totalCorner =sumarPorcentajesConsejoCorner(porcentajeLocal,porcentajeVisitante);
			if(totalCorner >= 120) {
				return true;
			}else {
				return false;
			}
		}catch(Exception e){
			System.out.println("No se ha podido calcular consejo corner" + e.getMessage());
			return false;
		}
	}
	public static boolean consejoCornerUnder (String porcentajeLocal,String porcentajeVisitante,Elements divPred) {
		try {
			Integer totalCorner =sumarPorcentajesConsejoCorner(porcentajeLocal,porcentajeVisitante);
			if(totalCorner >= 130) {
				return true;
			}else {
				return false;
			}
		}catch(Exception e){
			System.out.println("No se ha podido calcular consejo corner" + e.getMessage());
			return false;
		}
	}
	public static boolean esEsports (String titulo) {
		String[] cadenaTitulo = titulo.split(" ");
		boolean contieneEsports = false;
		boolean contieneEsoccer = false;
		for(String palabraTitulo: cadenaTitulo) {
			contieneEsports = palabraTitulo.equalsIgnoreCase("Esports");
			contieneEsoccer = palabraTitulo.equalsIgnoreCase("Esoccer");
			if(contieneEsoccer || contieneEsports) {
				break;
			}
		}
		if(contieneEsoccer || contieneEsports) {
			return true;
		}else {
			return false;
		}
	}
	
	public static String[] obtenerFactsEquipo (Element equipoFacts) {
		String goles[] = new String[9];
		Elements factsEquipo = equipoFacts.select("span.factValue");
		String equipoTodo = equipoFacts.select("p").get(0).text();
		String [] equipoTodoSplit = equipoTodo.split("last");
		String [] equipoLastSplit = equipoTodoSplit[1].split(" ");
		String victorias = factsEquipo.get(0).text();
		String [] victoriasSeparadas = victorias.split(" ");
	       goles[0] = factsEquipo.get(1).text();
	       goles[1] = factsEquipo.get(2).text();
	       goles[2] = factsEquipo.get(3).text();
	       goles[3] = factsEquipo.get(4).text();
	       goles[4] = victoriasSeparadas[0];
	       goles[5] = equipoLastSplit[1];
	   String [] golesMarcados = equipoTodoSplit[1].split("scored");
	   String [] numeroGolesMarcados = golesMarcados[1].split(" ");
	   String numeroDeGolesMarcados = numeroGolesMarcados[1];
	   		goles[6] = numeroDeGolesMarcados;
	   String golesConcedidos = numeroGolesMarcados[4];
	   		goles[7] = golesConcedidos;
	   		// empates
	   		goles[8] = victoriasSeparadas[4];
		return goles;
	}
	public static boolean favoritoLocal (String[] apuestasEquipos) {
		boolean localEsFavorito = false;
		Float cuotaLocalDecimal = Float.parseFloat(apuestasEquipos[0]);
	    Float cuotaVisitanteDecimal= Float.parseFloat(apuestasEquipos[2]);
	       if (cuotaLocalDecimal < cuotaVisitanteDecimal) {
	    	   localEsFavorito =  true;
	       }
	       if(cuotaVisitanteDecimal < cuotaLocalDecimal) {
	    	   localEsFavorito =  false;
	       }
	       return localEsFavorito;
	}
	public static boolean esPartidoCorner (String [] cuotaCorner) {
		if(cuotaCorner.length == 1) {
       		if(cuotaCorner[0].equals("")) {
       			return false;
       		}else {
       			return true;
       		}
       	}else {
       		return true;
       	}
	}
	public static Integer sumarPorcentajesConsejoCorner (String localPorcierto,String visitantePorciento) {
		Integer porcentajeLocalInt = pasarEntero(localPorcierto);
		Integer porcentajeVisitanteInt = pasarEntero(visitantePorciento);
		return porcentajeLocalInt + porcentajeVisitanteInt;
	}
	public static Integer sumarMediaGolesConsejoGoles (String localPorcierto,String visitantePorciento) {
		Integer porcentajeLocalInt = pasarEntero(localPorcierto);
		Integer porcentajeVisitanteInt = pasarEntero(visitantePorciento);
		return porcentajeLocalInt + porcentajeVisitanteInt;
	}
	
	public static Double consejoGoles (String [] datosLocal, String[] datosVisitante,boolean favoritoLocal,String lineaGolAsiatica,String [] datosBarritas) {
		try {
			String mediaGolesLocal = datosLocal[0];
			String mediaGolesEncajadosLocal = datosLocal[1];
			String mediaGolesVisitante = datosVisitante[0];
			String mediaGolesEncajadosVisitante = datosVisitante[1];
			Integer sumaDeGolesLocal = sumarMediaGolesConsejoGoles(mediaGolesLocal, mediaGolesEncajadosLocal);
			Integer sumaDeGolesVisitante = sumarMediaGolesConsejoGoles(mediaGolesVisitante, mediaGolesEncajadosVisitante);
			Double MediaGolesLocal = new Double(sumaDeGolesLocal);
			Double MediaGolesVisitante = new Double(sumaDeGolesVisitante);
			Double MediaGolesLocalReal = MediaGolesLocal/2;
			Double MediaGolesVisitanteReal = MediaGolesVisitante/2;
			Double lineaDeGolInt = Double.valueOf(lineaGolAsiatica)*10;
			//Integer lineaGolIntReal = lineaDeGolInt.intValue();
			return  (MediaGolesVisitanteReal + MediaGolesLocalReal) - lineaDeGolInt;
		}catch (Exception e) {
			System.out.println("No se ha podido calcular consejoGolesOver");
			return null;
		}
	}
	public static Integer consejoGolesUnder (String [] datosLocal, String[] datosVisitante,boolean favoritoLocal,String lineaGolAsiatica, String [] datosBarritas) {
		try {
			if(favoritoLocal) {
				String mediaGolesFavorito = datosLocal[0];
				String mediaGolesEncajadosNoFavorito = datosVisitante[1];
				Integer sumaBarrasAmarillas = sumarPorcentajesConsejoCorner(datosBarritas[2], datosBarritas[3]);
				Integer sumaDeGoles = sumarMediaGolesConsejoGoles(mediaGolesFavorito, mediaGolesEncajadosNoFavorito);
				Double lineaDeGolInt = Double.valueOf(lineaGolAsiatica)*10;
				Integer lineaGolIntReal = lineaDeGolInt.intValue();
				Integer lineaConDif = lineaGolIntReal - 3;
				if(sumaDeGoles < lineaConDif && sumaBarrasAmarillas >= 130 ) {
					return sumaDeGoles;
				}else {
					return null;
				}
				
			}else {
				String mediaGolesFavorito = datosVisitante[0];
				String mediaGolesEncajadosNoFavorito = datosLocal[1];
				Integer sumaBarrasAmarillas = sumarPorcentajesConsejoCorner(datosBarritas[2], datosBarritas[3]);
				Integer sumaDeGoles = sumarMediaGolesConsejoGoles(mediaGolesFavorito, mediaGolesEncajadosNoFavorito);
				Double lineaDeGolInt = Double.valueOf(lineaGolAsiatica)*10;
				Integer lineaGolIntReal = lineaDeGolInt.intValue();
				Integer lineaConDif = lineaGolIntReal - 3;
				if(sumaDeGoles < lineaConDif && sumaBarrasAmarillas >= 130 ) {
					return sumaDeGoles;
				}else {
					return null;
				}
			}
		}catch (Exception e) {
			System.out.println("No se ha podido calcular consejo goles under");
			return null;
		}
	}
	public static Double consejoHAFavoritos (boolean favoritoLocal,String [] cuotasMercado, String handicapAsia) {
		try {
			if(favoritoLocal) {
				String cuotaFavorito = cuotasMercado[0];
				Double valorCuota = Double.valueOf(cuotaFavorito);
				if(valorCuota != null ) {
					return valorCuota;
				}else {
					return null;
				}
				
			}else {
					String cuotaFavorito = cuotasMercado[2];
					Double valorCuota = Double.valueOf(cuotaFavorito);
					if(valorCuota != null ) {
						return valorCuota;
					}else {
						return null;
					}
				
				
			}
		}catch (Exception e) {
			System.out.println("No ha podido calcular consejo HAFavoritos");
			return null;
		}
	}
	public static Double consejoHANoFavoritos (boolean favoritoLocal,String [] cuotasMercado, String handicapAsia) {
		try {
			if(favoritoLocal) {
				String cuotaFavorito = cuotasMercado[0];
				Double valorCuota = Double.valueOf(cuotaFavorito);
					if(valorCuota >= 2.00 ) {
						return valorCuota;
					}else {
						return null;
					}
				
			}else {
				String cuotaFavorito = cuotasMercado[2];
				Double valorCuota = Double.valueOf(cuotaFavorito);
					if(valorCuota >= 2.00 ) {
							return valorCuota;
					}else {
							return null;
					}
				
				
			}
		}catch (Exception e) {
			System.out.println("No se ha podido calcular consejo HA No favoritos");
			return null;
		}
	}
	public static Double consejoEmpate (boolean favoritoLocal,String [] cuotasMercado, String handicapAsia,String lineaDeGol) {
		try {
			Double lineaDeGolAsiaticaValor = Double.valueOf(lineaDeGol);
			if(favoritoLocal) {
				if(!handicapAsia.isEmpty() && lineaDeGolAsiaticaValor <= 2.25 && ( handicapAsia.equals("0.0") || handicapAsia.equals("0.25") || handicapAsia.equals("-0.25")
						|| handicapAsia.equals("+0.0") || handicapAsia.equals("+0.25") )) {
				String cuotaFavorito = cuotasMercado[0];
				Double valorCuota = Double.valueOf(cuotaFavorito);
					if(valorCuota >= 2.40 ) {
						return valorCuota;
					}else {
						return null;
					}
				}else {
					return null;
				}
				
			}else {
				String cuotaFavorito = cuotasMercado[2];
				Double valorCuota = Double.valueOf(cuotaFavorito);
				if(!handicapAsia.isEmpty() && lineaDeGolAsiaticaValor <= 2.25 && (handicapAsia.equals("0.0") || handicapAsia.equals("0.25") || handicapAsia.equals("-0.25")
						|| handicapAsia.equals("+0.0") || handicapAsia.equals("+0.25"))) {
					if(valorCuota >= 2.40 ) {
							return valorCuota;
					}else {
							return null;
					}
				}else {
					return null;
				}
				
			}
		}catch (Exception e) {
			System.out.println("No se ha podido calcular consejo Empate");
			return null;
		}
	}
	
	public static PartidosHoy partidosPagina (int pagina) {
		URLConnection connection;
		try {
			dormirPrograma();
			connection = new URL("https://api.totalcorner.com/v1/match/today?token=3c85ac94522b5ac2&page="+pagina+"&type=upcoming&columns=events,odds,asian,cornerLine,goalLine,asianCorner,attacks").openConnection();
			InputStream is = connection.getInputStream();
			System.out.println("Calculando pagina "+pagina +" ....");
			statusLabel.setText("Calculando pagina "+pagina +" ....");
			BufferedReader in = new BufferedReader(new InputStreamReader(
					is));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			PartidosHoy partidosHoy2 = gson.fromJson(response.toString(), PartidosHoy.class);
			return partidosHoy2;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	public static void dormirPrograma () {
		 try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	public static String nombreCarpeta () {
		Calendar c1 = Calendar.getInstance();
		String dia = Integer.toString(c1.get(Calendar.DATE));
		String mes = Integer.toString(c1.get(Calendar.MONTH)+1);
		String annio = Integer.toString(c1.get(Calendar.YEAR));
		String hora = Integer.toString(c1.get(Calendar.HOUR_OF_DAY));
		String minutos = Integer.toString(c1.get(Calendar.MINUTE));
		return dia+mes+annio+"_"+hora+"_"+minutos;
	}
	public static String [] calculoBarrasGoles (Document document) {
		String [] barrasGoles = new String[4];
//		Elements barras = document.select("span.meter");
//		Element cornerOverPorciento = barras.get(4);
//		Element cornerOverEmpateLocal = barras.get(5);
//		Element cornerOverPorcientoVisitante = barras.get(6);
//		Element cornerOverEmpateVisitante = barras.get(7);
		Elements golesOverPorciento = document.select("#match_facts_container > div.col-xs-12.no-left-padding.no-right-padding > div > div:nth-child(4) > div:nth-child(1) > div > div:nth-child(3) > div > span:nth-child(1)");
		Elements golesOverEmpateLocal = document.select("#match_facts_container > div.col-xs-12.no-left-padding.no-right-padding > div > div:nth-child(4) > div:nth-child(1) > div > div:nth-child(3) > div > span.meter.draw");
		Elements golesOverPorcientoVisitante = document.select("#match_facts_container > div.col-xs-12.no-left-padding.no-right-padding > div > div:nth-child(4) > div:nth-child(2) > div > div:nth-child(3) > div > span:nth-child(1)");
		Elements golesOverEmpateVisitante = document.select("#match_facts_container > div.col-xs-12.no-left-padding.no-right-padding > div > div:nth-child(4) > div:nth-child(2) > div > div:nth-child(3) > div > span.meter.draw");
		// calculo barra azul
		barrasGoles[0] = limpiarPorcentaje(golesOverPorciento);
		barrasGoles[1] = limpiarPorcentaje(golesOverPorcientoVisitante);
		String porcentajeEmpateLocal = limpiarPorcentaje(golesOverEmpateLocal);
		String porcentajeEmpateVisitante = limpiarPorcentaje(golesOverEmpateVisitante);
		// calculo la barra amarilla
		barrasGoles[2] = calcularBarraAmarilla(porcentajeEmpateLocal, barrasGoles[0]);
		barrasGoles[3] = calcularBarraAmarilla(porcentajeEmpateVisitante, barrasGoles[1]);
			return barrasGoles;
	}
	public static void fill(int value) 
    { 

                    progressBar.setValue(value);

    } 
	private static void startThread()  {
		SwingWorker sw1 = new SwingWorker()  
        { 
  
            @Override
            protected String doInBackground() throws Exception  
            { 
            	List <PartidosResultado> urlPorVisitar = getUrlVisitar();
    			List <PartidoPdf> partidosImprimir = new ArrayList<PartidoPdf>();
    			List<String> listaPartidosFallados = new ArrayList<String>();
    			// guardo los fallados para un segundo intento
    		    try {
    		    	int totalPartidosEncontrados = urlPorVisitar.size();
    				int partidosVisitados = 0;
    				int totalBarra = 0;
    		        for (PartidosResultado partido : urlPorVisitar) {
    		        	partidosVisitados++;
    		        	totalBarra = (partidosVisitados*1000)/totalPartidosEncontrados;
    		                	 PartidoPdf partidoEnPdf = iniciarBot(partido);
    		                	 if (partidoEnPdf == null) {
    			                    	listaPartidosFallados.add("https://totalcorner.com/match/corner-stats/"+partido.getId());
    			                    }else {
    			                    	partidosImprimir.add(partidoEnPdf);
    			                    }
    		                	 // separamos pdf en bloques
    		                	 if( partidosVisitados % 50 == 0 && partidosVisitados != 0 ){
    		                		 	ByteArrayOutputStream outputStreamEmail = new ByteArrayOutputStream();
    		                		    crearPdfPartidos(partidosImprimir,outputStreamEmail);
    		                		}
    			                    // calculamos barra
    		        	 		final int totalbarraEstado = totalBarra;
    		        	 		System.out.println("Partido " + partidosVisitados + " de " + totalPartidosEncontrados);
    		        	 		statusLabel.setText("Partido " + partidosVisitados + " de " + totalPartidosEncontrados);
    		                    EventQueue.invokeLater(new Runnable() {
    		                        public void run() {
    		                        	progressBar.setValue(totalbarraEstado);
    		                        }
    		                      });
                  
    		        }
    		      com.itextpdf.text.Document documentoPdf = new com.itextpdf.text.Document();
    		      ByteArrayOutputStream outputStreamEmail2 = new ByteArrayOutputStream();
    				try {
    					
    		        	String path = new File(".").getCanonicalPath();
    		        	String FILE_NAME = path + "/"+nombreCarpeta()+"/CONSEJOS.pdf";
    		        	new File(path + "/"+nombreCarpeta()).mkdir();
    		        	FileOutputStream output = new FileOutputStream(new File(FILE_NAME));
    		            PdfWriter.getInstance(documentoPdf,output);
    		            PdfWriter.getInstance(documentoPdf, outputStreamEmail2);
    		            
    		        } catch (FileNotFoundException | DocumentException e) {
    		            e.printStackTrace();
    		        } catch (IOException e) {
    					e.printStackTrace();
    				}
    				documentoPdf.open();
    				for (PartidoPdf partido : partidosImprimir) {
    					if(partido != null) {
    						for (Object elementoPartido : partido.getElementos()) {
    							documentoPdf.add((com.itextpdf.text.Element) elementoPartido);
    						}
    					}
    				}
    		        documentoPdf.close();
    		        byte[] bytesEmail = outputStreamEmail2.toByteArray();
    		        EnvioEmail.enviar("CONSEJOS", bytesEmail);
    		        System.out.println("Email enviado");
    		        statusLabel.setText("Documento finalizado.");
    		        System.out.println("Documento finalizado.");
    		        try {
    		        	String pathError = new File(".").getCanonicalPath();
    		        	new File(pathError + "/"+nombreCarpeta()).mkdir();
    		        	String FILE_NAME_ERROR = pathError + "/"+nombreCarpeta()+"/partidosFallados.txt";
    		        	File fichero = new File(FILE_NAME_ERROR);
    		        	FileOutputStream fos = new FileOutputStream(fichero);
    		        	BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
    		        	 
    		        	for (String idPartido : listaPartidosFallados) {
    		        		bw.write(idPartido);
    		        		bw.newLine();
    		        	}
    		        	bw.close();
    		        	statusLabel.setText("Ejecución finalizada");
    		        	 System.out.println("Ejecución finalizada");
    		        	 btnSubmit.setEnabled(true);
    		        } catch (FileNotFoundException e) {
    		            e.printStackTrace();
    		        } catch (IOException e) {
    					e.printStackTrace();
    				}
    		    } catch (Exception e) {
    		        e.printStackTrace();
    		    }
				return null;
            } 
  
            @Override
            protected void process(List chunks) 
            { 
                // define what the event dispatch thread  
                // will do with the intermediate results received 
                // while the thread is executing 
                  
                //statusLabel.setText(String.valueOf(val)); 
            } 
 
        }; 
          
        // executes the swingworker on worker thread 
        sw1.execute();  
    } 
	public static Map<String, String> loguearTotal() {
		try {
//			Connection.Response loginForm = Jsoup.connect("https://www.totalcorner.com/user/login")
//			        .method(Connection.Method.GET)
//			        .execute();
			Connection.Response response = Jsoup.connect("https://www.totalcorner.com/user/login")
		            .method(Connection.Method.POST)
		            .data("username", "Morigeri")
		            .data("password", "Morigeri80")
		            .execute();


		    Map<String, String> cookies = response.cookies();
		    return cookies;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}
	public static String quitaDiacriticos(String s) {
	    s = Normalizer.normalize(s, Normalizer.Form.NFD);
	    s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
	    return s;
	}
	
	public static String obtenerConsejo(Elements divPredicciones,String consejo ) {
	  String predicciones = divPredicciones.text();
	 String [] prediccionesSplit = predicciones.split(",");
	 String consejoFinal = "";
	 for (String parte : prediccionesSplit) {
		 if(parte.contains(consejo)) {
			 if (parte.contains(":")) {
				String [] parteSplit = parte.split(":");
				consejoFinal = parteSplit[1];
			 }else {
				 consejoFinal = parte;
			 }
			 break;
		 }
	 }
	 return consejoFinal;
	}
	
	public static String calcularHandicapAsiatico (String [] handicapAsiaticoJson) {
		String handicapComas = handicapAsiaticoJson[0];
        String [] handicapAsiatico = handicapComas.split(",");
        String handicapAsiaticoFinal = "";
        if(handicapAsiatico.length > 1) {
        	if(!handicapAsiatico[0].equals("") && !handicapAsiatico[1].equals("") && handicapAsiatico[0] != null
        			&& handicapAsiatico[0] != null) {
			        	Double primerValor = Double.valueOf(handicapAsiatico[0]);
			        	Double segundoValor = Double.valueOf(handicapAsiatico[1]);
			        	Double total = primerValor + segundoValor;
			        	Double mediaValores = total/2;
			        	handicapAsiaticoFinal = Double.toString(mediaValores);
        	}
        }else if(handicapAsiatico.length == 1 && !handicapAsiatico[0].isEmpty() && handicapAsiatico[0] != null
        		&& !handicapAsiatico[0].equals("")){
        				handicapAsiaticoFinal = handicapAsiatico[0];
        }else{
        	handicapAsiaticoFinal = "";
        }
        return handicapAsiaticoFinal;
	}
	public static String calcularLineaGolAsiatica (PartidosResultado partido,String[] predicciones) {
		String lineaGolAsiatica = "";
		if(partido.getP_goal() != null || !partido.getP_goal()[0].equals("")) {
	    	   String [] lineaGolJson =  partido.getP_goal();
	    	   String lineaGolComa = lineaGolJson[0];
	    	   String[] lineaSeparada = lineaGolComa.split(",");
	    	   if(lineaSeparada.length > 1) {
	    		   Double cadena1 = Double.valueOf(lineaSeparada[0]);
	    		   Double cadena2 = Double.valueOf(lineaSeparada[1]);
	    		   Double sumaCadenas = cadena1+cadena2;
	    		   lineaGolAsiatica = Double.toString(sumaCadenas/2);
	    	   }else {
	    		   lineaGolAsiatica = lineaSeparada[0];
	    	   }
	       }else {
	    	   lineaGolAsiatica = predicciones[4].replace(",", ""); 
	       }
		return lineaGolAsiatica;
	}
	public static Integer calcularDiferenciaVictorias (boolean esLocalConsejo,String [] datosEquipoLocal,String [] datosEquipoVisitante) {
		Integer diferenciaVictorias = null;
		if(esLocalConsejo) {
        	try {
        		int victoriasLocalInt = Integer.parseInt(datosEquipoLocal[4]);
        		int victoriasVisitanteInt = Integer.parseInt(datosEquipoVisitante[4]);
        		diferenciaVictorias = victoriasLocalInt - victoriasVisitanteInt;
        	}catch (Exception e) {
				System.out.println("No se ha podido parsear (diferencia victorias) : "+datosEquipoLocal[4] + " y " + datosEquipoVisitante[4] );
			}
        }
        if(!esLocalConsejo) {
        	try {
        		int victoriasLocalInt = Integer.parseInt(datosEquipoLocal[4]);
        		int victoriasVisitanteInt = Integer.parseInt(datosEquipoVisitante[4]);
        		diferenciaVictorias = victoriasVisitanteInt - victoriasLocalInt;
        	}catch (Exception e) {
				System.out.println("No se ha podido parsear (diferencia victorias)");
			}
        }
        return diferenciaVictorias;
	}
	public static Double stringTodouble (String texto) {
		double d;
		try {
			d=Double.parseDouble(texto);
		} catch (Exception e) {
			System.out.println("No se ha podido parsear de string a double");
			return null;
		}
		return d;
	}
	public static Double calcularSumaCorners (String lineaCornerAsiatica, String url) {
		ScrapeUtils login = new ScrapeUtils();
   		WebDriver logged = login.login(url,listAgent);
   		logged.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
   		List<WebElement> ElementsHome =  logged.findElements(By.className("home_stats_table"));
   		List<WebElement> ElementsAway =  logged.findElements(By.className("away_stats_table"));
   		List<WebElement> trHome = ElementsHome.get(0).findElements(By.tagName("tr"));
   		List<WebElement> trAway = ElementsAway.get(0).findElements(By.tagName("tr"));
   		WebElement tdHomeHome = trHome.get(3);
   		WebElement tdAwayAway = trAway.get(4);
   		// media corners local
   		WebElement avgHome = tdHomeHome.findElements(By.tagName("td")).get(3);
   		// media corners visitante
   		WebElement avgAway = tdAwayAway.findElements(By.tagName("td")).get(3);
   		String mediaLocal = avgHome.getText();
   		String mediaVisitante = avgAway.getText();
   		Double mediaLocalDecimales = stringTodouble(mediaLocal);
   		Double mediaVisitanteDecimales = stringTodouble(mediaVisitante);
   		Double lineaCornerDecimales = stringTodouble(lineaCornerAsiatica);
   		Double totalSumaCorners = (mediaLocalDecimales + mediaVisitanteDecimales) - lineaCornerDecimales;
   		logged.quit();
   		return totalSumaCorners;
	}
	public static List<PartidosResultado> getUrlVisitar () {
		List <PartidosResultado> listaIds = new ArrayList<PartidosResultado>();
		try {
			URLConnection connection;
			connection = new URL("https://api.totalcorner.com/v1/match/today?token=3c85ac94522b5ac2&type=upcoming&columns=events,odds,asian,cornerLine,goalLine,asianCorner,attacks").openConnection();
			InputStream is = connection.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					is));
			String inputLine;
			StringBuffer response = new StringBuffer();
	
			while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
			}
			in.close();
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			PartidosHoy partidosHoyCalcularPagina = gson.fromJson(response.toString(), PartidosHoy.class);
			int paginas = 1;
			if(partidosHoyCalcularPagina != null) {
				paginas = partidosHoyCalcularPagina.getPagination().getPages();
			}
			for(int p = 1;p <= paginas ; p++)      {
		        Thread.sleep(5000);
				PartidosHoy partidosHoy2 = new PartidosHoy();
				if(p == 1) {
					 partidosHoy2 = partidosHoyCalcularPagina;
				}else {
					 partidosHoy2 = partidosPagina(p);
				}
				if(partidosHoy2.getSuccess() == 1) {
					// genero la lista de ids de los partidos que tengo que consultar.
						for(PartidosResultado partido : partidosHoy2.getData()) {
							listaIds.add(partido);
						}
				
				}
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		return listaIds;
	}
	public static void crearPdfPartidos (List<PartidoPdf> partidosImprimir,ByteArrayOutputStream email) {
		com.itextpdf.text.Document documentoPdf = new com.itextpdf.text.Document();
		int numeroPartidosLista = partidosImprimir.size();
		try {
			
        	String path = new File(".").getCanonicalPath();
        	String FILE_NAME = path + "/"+nombreCarpeta()+"/CONSEJOS-Primeros"+numeroPartidosLista+".pdf";
        	new File(path + "/"+nombreCarpeta()).mkdir();
            PdfWriter.getInstance(documentoPdf, new FileOutputStream(new File(FILE_NAME)));
            PdfWriter.getInstance(documentoPdf, email);
        } catch (FileNotFoundException | DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
			e.printStackTrace();
		}
		documentoPdf.open();
		for (PartidoPdf partido : partidosImprimir) {
			if(partido != null) {
				for (Object elementoPartido : partido.getElementos()) {
					try {
						documentoPdf.add((com.itextpdf.text.Element) elementoPartido);
					} catch (DocumentException e) {
						System.out.println("Documento no creado.");
						e.printStackTrace();
					}
				}
			}
		}
        documentoPdf.close();
        byte[] bytesEmail = email.toByteArray();
        EnvioEmail.enviar("CONSEJOS-Primeros"+numeroPartidosLista, bytesEmail);
        System.out.println("Documento finalizado.");
	}
	public static Integer pasarInt (String dato) {
		try {
			 return Integer.parseInt(dato);
		}catch (NumberFormatException e) {
			return null;
		}catch (Exception h) {
			return null;
		}
	}
}
