package org.slosc.wsdl2rest.samples;

//import junit.framework.TestCase;
//import org.codehaus.xfire.spring.example.Echo;
//import org.codehaus.xfire.spring.example.A;
//import org.codehaus.xfire.spring.example.B;
//import org.codehaus.xfire.client.XFireProxyFactory;
//import org.codehaus.xfire.service.Service;
//import org.codehaus.xfire.service.binding.ObjectServiceFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.slosc.wsdl2rest.samples.StockQuoteService;


public class Test {

    private StockQuoteService service;
    private StockQuoteService servicexFire;

    protected void setUp() throws Exception {
        ApplicationContext ctx = new ClassPathXmlApplicationContext(new String[] {"applicationContext.xml"});
        service = (StockQuoteService) ctx.getBean("StockQuoteService");


//        Service serviceModel = new ObjectServiceFactory().create(StockQuoteService.class);
//        servicexFire = (StockQuoteService) new XFireProxyFactory().create(serviceModel, "http://localhost:8080/springWeb/StockQuoteServiceService");
//        Client client = ((XFireProxy) Proxy.getInvocationHandler(soapTest)).getClient();
//        client.setProperty(Channel.USERNAME, "test");
//        client.setProperty(Channel.PASSWORD, "test");

//        super.setUp();
    }

    public void testRemoteAPI(){
        
        System.out.println(service.getStockQuote("XYZ"));
    }
}
