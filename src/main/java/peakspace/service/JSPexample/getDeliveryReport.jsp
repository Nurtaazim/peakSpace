<%@ page language="java" contentType="text/html; charset=Cp1251"
         pageEncoding="Cp1251"
         import="java.util.*, java.io.*, java.net.*"  %>
<% 
	/**  
	* ����� ��� ������� � ��������� smspro.nikita.kg.  
	* */
	String login = "arstanbeekovvv@gmail.com";
	/** 
	* ������ ��� ������� � ��������� smspro.nikita.kg. 
	* */
	String password = "mirlan001m.";
	/** 
	* ID ����������, ������� ������������� ��� �������� ���.
	* */
	String transactionId = "SK36c5a0c6fc5df79afa0cc72a9a8d8102";
        
	String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
		"<dr>"+		
			"<login>" + login + "</login>" +
			"<pwd>" + password + "</pwd>" +
			"<id>" + transactionId + "</id>" +      
		"</dr>";
	/**
	* ���� �������� ������������� ����� �� ���������� ���������, �� ����� ��������� ����� ��� ������ ����������� ��������.
	* */
	/**
	String phone = "996550403993";
	String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
	"<dr>"+		
	"<login>" + login + "</login>" +
	"<pwd>" + password + "</pwd>" +
	"<id>" + transactionId + "</id>" +
			"<phone>" + phone + "</phone>" + 
	"</dr>";
	*/
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>�������� ��������� ������ � �������� ������������� ��� ����� ���� smspro.nikita.kg</title>
</head>
<body>
<h1>�������� ��������� ������ � �������� ������������� ��� ����� ���� smspro.nikita.kg</h1>
<hr>
<h4>������������ XML-������:</h4>
<code><%= xml.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("&gt;&lt;", "&gt;<br>&lt;") %></code>
<h4>���������:</h4>
<%
	try {
            URL url = new URL("http://smspro.nikita.kg/api/dr");
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