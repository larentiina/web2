package servlets;

import models.Coordinates;
import models.PointData;
import models.PointsCollection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet(name="servlets.AreaCheckServlet", value = "/check")
public class AreaCheckServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
        long startTime = System.currentTimeMillis();
        double x = Double.parseDouble(req.getParameter("x"));
        double y = Double.parseDouble(req.getParameter("y"));
        double r = Double.parseDouble(req.getParameter("r"));
        PrintWriter out = resp.getWriter();
        out.println(makeResponse(x,y,r,isHit(x,y,r),startTime));
        resp.setStatus(HttpServletResponse.SC_OK);
        }catch (NumberFormatException e){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid parameter");
        }

    }
    private String isHit(double x, double y, double r){
        if (Math.pow(x,2)+Math.pow(y,2)<=Math.pow(r,2) && x<=0 && y>=0){
            return "Hit";
        } else if (Math.abs(x)<=(r/2) && Math.abs(y)<=r && x>=0 && y<=0) {
            return "Hit";
        } else if(y<=-x+r && x>=0 && y>=0){
            return "Hit";
        }else return "Not Hit";
    }
    private String makeResponse(double x, double y, double r, String isHit, long startTime){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        long endTime = System.currentTimeMillis();
        double totalTime = (double)(endTime - startTime)/1000;
        PointData pointData = new PointData(new Coordinates(x,y,r),isHit(x,y,r),dateFormat.format(date),totalTime);
        addPointInContext(pointData);
       return x+";"+y+";"+r+";"+ isHit +";"+dateFormat.format(date)+";"+ totalTime;}
    private void addPointInContext(PointData point){
        PointsCollection collectIon = (PointsCollection) getServletContext().getAttribute("PointsCollection");
        collectIon.addElement(point);
    }

}
