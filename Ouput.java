import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@WebServlet("/Ouput")
public class Ouput extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter print=response.getWriter();
        FindOutput objForFindOutput=new FindOutput();
        String output="";
        String json=request.getParameter("JSON");
        if(objForFindOutput.isCorrectSyntax(json))
        {
            output=objForFindOutput.getOutput();
            for(int i=0;i<output.length();i++)
            {
                if(output.charAt(i)=='\n')
                    print.print("<br>");
                else
                    print.print(output.charAt(i));
            }
        }
        else
            print.print("Check JSON code");
    }
}