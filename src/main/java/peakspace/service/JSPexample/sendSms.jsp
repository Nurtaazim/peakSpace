<%@ page language="java" contentType="text/html; charset=Cp1251"
         pageEncoding="Cp1251"
         import="java.util.*, java.io.*, java.net.*"  %>
<% 
	/**  
	* ����� ��� ������� � ��������� smspro.nikita.kg.  
	* */
	String login = "arstanbekovmirlan17";
	/** 
	* ������ ��� ������� � ��������� smspro.nikita.kg. 
	* */
	String password = "ipSB7U4A";
	/** 
	* ID ����������, ������� ������������� ��� �������� ���.
	* */
	String transactionId = "bfc87";
        
	/**
	* ��� ����������� - ������ ���� ����������� � ��������������� smspro.nikita.kg
	* */
	String sender = "SMSPRO.KG";
	/** 
	* ����� ���-��������� - ����� �� ������� ��� �������� ����� ����� (�� 800 ������). 
	* � ������ ������������� ��������� smspro.nikita.kg ������������� �������� ����� �� ��������� ���������. 
	* */
	String text = "test from peak space";
	/** 
	* ����� �������� ���������� ��� � ������� 996���������. 
	* � ����� ���������� �������� ����� ���� ������� � ����� 1�� ��������.
	* */
	String phone = "996771900091";
           
	String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
		"<message>" +
			"<login>" + login + "</login>" +
			"<pwd>" + password + "</pwd>" +
			"<id>" + transactionId + "</id>" +
			"<sender>" + sender + "</sender>" +
			"<text>" + new String(text.getBytes("UTF-8")) + "</text>" + 
			"<phones>" +
			"<phone>" + phone + "</phone>" + 
			"</phones>" +       
		"</message>";
        
	/**
	* �������� ��������� � ��������� ������� �������� � ������� YYYYMMDDHHMMSS.
	* */
	/**
	String time = "20240101123000";
	String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
	"<message>" +
		"<login>" + login + "</login>" +
		"<pwd>" + password + "</pwd>" +
		"<id>" + transactionId + "</id>" +
		"<sender>" + sender + "</sender>" +
		"<text>" + new String(text.getBytes("UTF-8")) + "</text>" + 
					"<time>" + time + "</time>" + 
		"<phones>" +
		"<phone>" + phone + "</phone>" + 
		"</phones>"+       
	"</message>";	
	*/
        
	/**
	* �������� ��������� �� ��������� �������.
	* */
	/**
	String[] phones = {"996550403993", "996779377888"};
	String phonesXml = "<phones>";
	for (String p : phones) {
		phonesXml += "<phone>" + p + "</phone>";
	}
	phonesXml += "</phones>";
	String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
	"<message>" +
		"<login>" + login + "</login>" +
		"<pwd>" + password + "</pwd>" +
		"<id>" + transactionId + "</id>" +
		"<sender>" + sender + "</sender>" +
		"<text>" + new String(text.getBytes("UTF-8")) + "</text>" + 
		phonesXml +     
	"</message>";
	*/
         
	/**
	* �������� ��������� ���������, ��������� �� ����� ���������� ���������� � �� ����� ��������������.
	* */
	/** 
	String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
	"<message>" +
		"<login>" + login + "</login>" +
		"<pwd>" + password + "</pwd>" +
		"<id>" + transactionId + "</id>" +
		"<sender>" + sender + "</sender>" +
		"<text>" + new String(text.getBytes("UTF-8")) + "</text>" + 
		"<phones>" +
		"<phone>" + phone + "</phone>" + 
		"</phones>" +  
					"<test>1</test>" +
	"</message>";
	*/
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>�������� �������� SMS ����� ���� smspro.nikita.kg</title>
</head>
<body>
<h1>�������� �������� SMS ����� ���� smspro.nikita.kg</h1>
<hr>
<h4>������������ XML-������:</h4>
<code><%= xml.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("&gt;&lt;", "&gt;<br>&lt;") %></code>
<h4>���������:</h4>
<%
	try {
            URL url = new URL("http://smspro.nikita.kg/api/message");
            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            huc.setDoOutput(true);
            huc.setDoInput(true);
            huc.setRequestMethod("POST");
            huc.setRequestProperty("Content-Type","application/xml");
            /** ��������� keep-alive. */
            /**
            huc.setRequestProperty("Connection", "Keep-Alive");
            huc.setRequestProperty("Keep-Alive", "timeout=5, max=1000");
            */
            OutputStream os = huc.getOutputStream();
            os.write(xml.getBytes());
            os.flush();
            os.close();
            InputStream is = huc.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            StringBuffer sb = new StringBuffer();
            String s;
            while ((s = br.readLine()) != null) {
                    sb.append(s);
            }
            br.close();
            huc.disconnect();
            String responseXml = sb.toString()
                .replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;")
                .replaceAll("&gt;&lt;", "&gt;<br>&lt;");
    	%>
    	����� �������:<br>
    	<code><%= responseXml %></code>
    	<%    	
	} catch (Exception ex) { }
%>
</body>
</html>