package es.joaquinjimenezgarcia;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import com.sun.istack.internal.logging.Logger;

/**
 * Servlet implementation class AutoresServlet
 */
@WebServlet("/AutoresServlet")
public class AutoresServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DataSource pool;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AutoresServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    public void init(ServletConfig config) throws ServletException {
		try {
			InitialContext ctx = new InitialContext();
			pool = (DataSource) ctx.lookup("java:comp/env/jdbc/LibreriaOnline");
			
			if (pool == null) {
				throw new ServletException("DataSource desconocida'mysql_tiendalibros'");
			}
		} catch (NamingException ex) {
			Logger.getLogger(LoginServlet.class.getName(), null).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
   protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		Connection conn = null;
		Statement stmt = null;
		
		try {
			conn = pool.getConnection();
			stmt = conn.createStatement();
			
			out.println("<html>");
			out.println("<head>");
			out.println("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css\" integrity=\"sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm\" crossorigin=\"anonymous\">");
			out.println("<title>Autores</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<div id=\"main\" class=\"container\">");
			out.println("<h2>Lista de Autores</h2>");
		
			String usuario;
			HttpSession session = request.getSession(false);
		
			if (session == null) {
				out.println("<h3>No has iniciado sesi�n</h3>");
			} else {
				synchronized (session) {
					usuario = (String) session.getAttribute("usuario");
				}
				
				out.println("<table>");
				out.println("<tr>");
				out.println("<td>Usuario:</td>");
				out.println("<td>" + usuario + "</td>");
				out.println("</tr>");
				out.println("</table>");
				out.println("<p><a href='logout'>Salir</a></p>");
				
				try {
					stmt = (Statement) conn.createStatement();
					String sqlStr = "SELECT idAutor, NombreAutor, FechaAltaAutor FROM Autor";
					
					out.println("<html>");
					out.println("<head><title>Autores</title></head>");
					out.println("<body>");
					out.println("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css\" integrity=\"sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm\" crossorigin=\"anonymous\">");
					
					ResultSet rs = stmt.executeQuery(sqlStr);
					int count = 0;
					
					out.println("<a href=\"autores.html\">Volver</a>");
					out.println("<table class=\"table\">");
					out.println("<thead class=\"thead-dark\">");
					out.println("<tr>");
					out.println("<th scope=\"col\">ID</th>");
					out.println("<th scope=\"col\">Nombre</th>");
					out.println("<th scope=\"col\">Fecha de Alta</th>");
					out.println("</tr>");
					out.println("</thead>");
					
					while (rs.next()) {
						out.println("<tbody>");
						out.println("<tr>" + "<td>" + rs.getString("idAutor") + "</td>");
						out.println("<td>" + rs.getString("NombreAutor")  + "</td>");
						out.println("<td>" + rs.getString("FechaAltaAutor") + "</td>" + "</tr>");
						out.println("</tbody>");
						
						count++;
					}
					
					out.println("</table>");
					out.println("<p>" + count + " autores encontrados.</p>");
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			
			out.println("</body>");
			out.println("</html>");
		}catch (SQLException ex) {
			out.println("<p>Servicio no disponible</p>");
			out.println("</div>");
			out.println("</body>");
			out.println("</html>");
			
			Logger.getLogger(LoginServlet.class.getName(), null).log(Level.SEVERE, null, ex); 
		} finally {
			out.close();
			
			try {
				if (stmt != null) {
					stmt.close();
				}
				
				if (conn != null) {
					conn.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
