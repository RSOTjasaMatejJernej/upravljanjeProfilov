
package si.fri.rso.upravljanjeprofilov;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static javax.ws.rs.core.HttpHeaders.USER_AGENT;


@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("upravljanjeProfilov")
public class UpravljanjeProfilovResources {

    @GET
    @Path("{profilId}")
    public Response getCustomer(@PathParam("profilId") String profilId) {
        //addCustomers();
        // List<Customer> customers = Database.getCustomers();

        try {
            String response = sendGet(profilId);

            return Response.ok(response).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.ok("exception").build();
        }

    }

    private String sendGet(String profilId) throws Exception {

        String url = "http://192.168.99.100:8080/v1/katalogProfilov/"+profilId;

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());

        return response.toString();

    }

    /*@GET
    public Response getAllProfils() {
        List<Profil> profils = Database.getProfils();
        return Response.ok(profils).build();
    }

    @GET
    @Path("{profilId}")
    public Response getProfil(@PathParam("profilId") String profilId) {
        Profil profil = Database.getProfil(profilId);
        return profil != null
                ? Response.ok(profil).build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }*/

    @POST
    public Response addNewProfil(Profil profil) {
        Database.addProfil(profil);
        return Response.ok(profil).build();
    }

    @DELETE
    @Path("{profilId}")
    public Response deleteProfil(@PathParam("profilId") String profilId) {
        Database.deleteProfil(profilId);
        return Response.ok(Response.Status.OK).build();
    }
}
