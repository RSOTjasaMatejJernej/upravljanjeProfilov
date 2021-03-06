
package si.fri.rso.upravljanjeprofilov;

import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.fault.tolerance.annotations.CommandKey;
import com.kumuluz.ee.fault.tolerance.annotations.GroupKey;
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
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.ws.rs.core.GenericType;

import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.faulttolerance.Bulkhead;

import static javax.swing.UIManager.get;
import static javax.ws.rs.core.HttpHeaders.USER_AGENT;


@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("upravljanjeProfilov")
@RequestScoped
@GroupKey("customers")
public class UpravljanjeProfilovResources {

    private Logger log = LogManager.getLogger(UpravljanjeProfilovResources.class.getName());
    private Client httpClient;

    @Inject
    @DiscoverService("katalogProfilov")
    private String baseUrl;

    @PostConstruct
    @Fallback(fallbackMethod = "getAllProfilsFallback")
    @Timeout(value = 2, unit = ChronoUnit.SECONDS)
    @CommandKey("find-customers")
    private void init() {
        httpClient = ClientBuilder.newClient();
    }

    @GET
    public Response getAllProfils() {
        return Response.ok("test").build();
        /*try {
            WebTarget wt = httpClient.target(baseUrl + "/v1/katalogProfilov/");
            Invocation.Builder b = wt.request();
            List<Profil> resp = b.get(new GenericType<Profil>(){});
            Profil response = b.get(new GenericType<Profil>() {
            });
            System.out.println("response je: " + response.toString());

            return Response.ok(response).build();
        }
        catch (Exception e) {
            //log.error(e);
            throw e;
        }*/
    }


    @GET
    @Path("{profilId}")
    @CircuitBreaker
    @Timeout(value = 2, unit = ChronoUnit.SECONDS)
    @Fallback(fallbackMethod = "getAllProfilsFallback")
    @CommandKey("find-customers")
    public Profil getCustomer(@PathParam("profilId") String profilId) {
        WebTarget wt = httpClient.target(baseUrl + "/v1/katalogProfilov/" + profilId);
        Invocation.Builder b = wt.request();
        Profil response = b.get(new GenericType<Profil>() {
        });
        System.out.println("response je: " + response.toString());

        return response;
    }

    public Profil getAllProfilsFallback(String profilId) {
        Profil response = new Profil();
        response.setId("Mikrostoritev ni na voljo.");

        System.out.println("response je: delaa" + response.toString());

        return response;
    }




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
