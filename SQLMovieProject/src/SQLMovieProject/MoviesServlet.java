package SQLMovieProject.src.SQLMovieProject;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// For JSON output
import org.json.JSONArray;
import org.json.JSONObject;


@WebServlet("/movies")
public class MoviesServlet extends HttpServlet {
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/yourDB", "user", "password");
      stmt = conn.prepareStatement("SELECT * FROM movies");
      rs = stmt.executeQuery();

      JSONArray movies = new JSONArray();
      while (rs.next()) {
        JSONObject movie = new JSONObject();
        movie.put("id", rs.getInt("id"));
        movie.put("title", rs.getString("title"));
        movie.put("genre", rs.getString("genre"));
        movie.put("poster", rs.getString("poster_url"));
        movies.put(movie);
      }

      out.print(movies.toString());
    } catch (Exception e) {
      e.printStackTrace();
      out.print("[]");
    } finally {
      try { if (rs != null) rs.close(); } catch (Exception ignored) {}
      try { if (stmt != null) stmt.close(); } catch (Exception ignored) {}
      try { if (conn != null) conn.close(); } catch (Exception ignored) {}
    }
  }
}
