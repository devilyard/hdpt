
/**
 * WebServiceEntryServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package com.bsoft.adapter.ws;

    /**
     *  WebServiceEntryServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class WebServiceEntryServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public WebServiceEntryServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public WebServiceEntryServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for startWs method
            * override this method for handling normal response from startWs operation
            */
           public void receiveResultstartWs(
                    com.bsoft.adapter.ws.WebServiceEntryServiceStub.StartWsResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from startWs operation
           */
            public void receiveErrorstartWs(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for transportData method
            * override this method for handling normal response from transportData operation
            */
           public void receiveResulttransportData(
                    com.bsoft.adapter.ws.WebServiceEntryServiceStub.TransportDataResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from transportData operation
           */
            public void receiveErrortransportData(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for invoke method
            * override this method for handling normal response from invoke operation
            */
           public void receiveResultinvoke(
                    com.bsoft.adapter.ws.WebServiceEntryServiceStub.InvokeResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from invoke operation
           */
            public void receiveErrorinvoke(java.lang.Exception e) {
            }
                


    }
    