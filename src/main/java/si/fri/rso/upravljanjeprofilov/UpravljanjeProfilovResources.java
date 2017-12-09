
package si.fri.rso.upravljanjeprofilov;

import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.logs.*;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.inject.Inject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.ws.rs.core.GenericType;

import static javax.swing.UIManager.get;
import static javax.ws.rs.core.HttpHeaders.USER_AGENT;


@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("upravljanjeProfilov")
@RequestScoped
public class UpravljanjeProfilovResources {

    private Logger log = LogManager.getLogger(UpravljanjeProfilovResources.class.getName());
    private Client httpClient;

    @Inject
    @DiscoverService("katalogProfilov")
    private String baseUrl;

    @PostConstruct
    private void init() {
        httpClient = ClientBuilder.newClient();
    }

    @GET
    @Path("{profilId}")
    public Profil getCustomer(@PathParam("profilId") String profilId) {
        log.debug(baseUrl + "/v1/katalogProfilov?" + profilId);

       try {
           WebTarget wt = httpClient.target(baseUrl + "/v1/katalogProfilov/" + profilId);
           Invocation.Builder b = wt.request();
           Profil response = b.get(new GenericType<Profil>() {
           });
           System.out.println("response je: " + response.toString());

            return response;
        } catch (Exception e) {
            //log.error(e);
            throw e;
        }}/*
        try {
            String response = sendGet(profilId);

            return Response.ok(response).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.ok("exception").build();
        }*/


    private String sendGet(String profilId) throws Exception {



        URL obj = new URL(baseUrl+ "/v1/katalogProfilov?" + profilId);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + baseUrl);
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
