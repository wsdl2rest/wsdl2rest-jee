package org.slosc.wsdl2rest.samples;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Context;

/**
 * A resource class is a Java class that uses JAX-RS annotations to implement a corresponding Web resource.
 * Resource classes are POJOs that have at least one method annotated with @Path or a request method designator.
 *
 * Resource methods MUST NOT have more than one parameter that is not annotated with one of the listed annotations.
 * 
 * A public constructor MAY include parameters annotated with one of the following: @Context, @HeaderParam,
 * @CookieParam, @MatrixParam, @QueryParam or @PathParam. However, depending on the resource class lifecycle and
 * concurrency, per-request information may not make sense in a constructor. If more than one public constructor is
 * suitable then an implementation will use the one with the most parameters. Choosing amongst suitable constructors
 * with the same number of parameters will generate a warning about ambiguity.
 */

public class StockQuoteServiceImpl implements StockQuoteService {

    private String contex;
    private String headerParam;
    private String cookie;
    private String matricParam;
    private String queryParam;
    private String pathParam;

    /*
    * @MatrixParam Extracts the value of a URI matrix parameter.
    * @QueryParam Extracts the value of a URI query parameter.
    * @PathParam Extracts the value of a URI template parameter.
    * @CookieParam Extracts the value of a cookie.
    * @HeaderParam Extracts the value of a header. 
    * @Context Injects an instance of a supported resource, see chapters 5 and 6 for more details.
    */
    //@Context, @HeaderParam, @CookieParam, @MatrixParam, @QueryParam or @PathParam
    public StockQuoteServiceImpl(String  contex, String headerParam, String cookie,
                                 String matricParam, String queryParam, String pathParam){
        this.contex = contex;       
        this.headerParam = headerParam;
        this.cookie = cookie;
        this.matricParam = matricParam;
        this.queryParam = queryParam;
        this.pathParam = pathParam;
    }

    //resource methods

    /**
     *
     * @param ticker
     * @return Results in an entity body mapped from the class of the returned instance. If the return value is not
     * null a 200 status code is used, a null return value results in a 204 status code.
     */
    public StockQuote getStockQuote(String ticker) {
        return null;
    }

    public StockQuote[] getStockQuoteHistory(String ticker, String fromDate, String toDate) {
        return new StockQuote[0];  
    }

    /**
     *
     * @param setMe
     *
     * @return Results in an empty entity body with a 204 status code.
     */
    public void setXYZ(String setMe){
        throw new UnsupportedOperationException();  //TODO change body of implemented method    
    }

    /**
     * Methods that need to provide additional metadata with a response should return an instance of Response,
     * the ResponseBuilder class provides a convenient way to create a Response instance using a builder pattern.
     * 
     * @param setMe
     * @return Results in an entity body mapped from the entity property of the Response with the status code
     * specified by the status property of the Response. A null return value results in a 204 status code.
     * If the status property of the Response is not set: a 200 status code is used for a non-null entity property
     * and a 204 status code is used if the entity property is null.
     */
    public Response foo(String setMe){
        throw new UnsupportedOperationException();  //TODO change body of implemented method
    }

    /**
     *
     * @param setMe
     * @return Results in an entity body mapped from the Entity property of the GenericEntity. If the return value is
     * not null a 200 status code is used, a null return value results in a 204 status code.
     */
    public GenericEntity bar(String setMe){
        throw new UnsupportedOperationException();  //TODO change body of implemented method
    }




    //bean properties
    public String getContex() {
        return contex;
    }

    public void setContex(String contex) {
        this.contex = contex;
    }

    public String getHeaderParam() {
        return headerParam;
    }

    public void setHeaderParam(String headerParam) {
        this.headerParam = headerParam;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getMatricParam() {
        return matricParam;
    }

    public void setMatricParam(String matricParam) {
        this.matricParam = matricParam;
    }

    public String getQueryParam() {
        return queryParam;
    }

    public void setQueryParam(String queryParam) {
        this.queryParam = queryParam;
    }

    public String getPathParam() {
        return pathParam;
    }

    public void setPathParam(String pathParam) {
        this.pathParam = pathParam;
    }
}
