<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import = "java.sql.*" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Database SQL Load</title>
    </head>
    <style>
        .error {
            color: red;
        }
        pre {
            color: green;
        }
    </style>
    <body>
        <h2>Database SQL Load</h2>
        <%
            /* How to customize:
             * 1. Update the database name on dbname.
             * 2. Create the list of tables, under tablenames[].
             * 3. Create the list of table definition, under tables[].
             * 4. Create the data into the above table, under data[]. 
             * 
             * If there is any problem, it will exit at the very first error.
             */
            String dbname = "sob_grup_43";
            String schema = "ROOT";
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            /* this will generate database if not exist */
            Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/" + dbname, "root", "root");
            Statement stmt = con.createStatement();
            
            /* inserting data */
            /* you have to exclude the id autogenerated from the list of columns if you have use it. */
            String data[] = new String[]{
                "INSERT INTO " + schema + ".TOPIC VALUES (NEXT VALUE FOR TOPIC_GEN, 'Computer Science')",
                "INSERT INTO " + schema + ".TOPIC VALUES (NEXT VALUE FOR TOPIC_GEN, 'Bases de dades')",
                "INSERT INTO " + schema + ".TOPIC VALUES (NEXT VALUE FOR TOPIC_GEN, 'Introduction to Java')",
                "INSERT INTO " + schema + ".CREDENTIALS VALUES (NEXT VALUE FOR CREDENTIALS_GEN, 'sob', 'sob')",
                "INSERT INTO " + schema + ".USUARI (ID, NOM, DNI) VALUES (NEXT VALUE FOR USUARI_GEN, 'Joan', '12345678A')",
                "INSERT INTO " + schema + ".USUARI (ID, NOM, DNI) VALUES (NEXT VALUE FOR USUARI_GEN, 'Maria', '98765432B')",
                "INSERT INTO " + schema + ".CREDENTIALS (ID, USERNAME, PASSWORD) VALUES (NEXT VALUE FOR CREDENTIALS_GEN, 'pere123', '12hola34')",
                "INSERT INTO " + schema + ".CREDENTIALS (ID, USERNAME, PASSWORD) VALUES (NEXT VALUE FOR CREDENTIALS_GEN, 'laura<3', '1234')",
                "INSERT INTO " + schema + ".USUARI (ID, NOM, DNI, TELEF, USERNAME) VALUES (NEXT VALUE FOR USUARI_GEN, 'Pere', '87654321C',  123456789, 'pere123')",
                "INSERT INTO " + schema + ".USUARI (ID, NOM, DNI, TELEF, USERNAME) VALUES (NEXT VALUE FOR USUARI_GEN, 'Laura', '11223344D', 987654321, 'laura<3')"
            };
            for (String datum : data) {
                if (stmt.executeUpdate(datum)<=0) {
                    out.println("<span class='error'>Error inserting data: " + datum + "</span>");
                    return;
                }
                out.println("<pre> -> " + datum + "</pre>");        //si no va treu el / del segon <pre>
            }
        %>
        <button onclick="window.location='<%=request.getSession().getServletContext().getContextPath()%>'">Go home</button>
    </body>
</html>
