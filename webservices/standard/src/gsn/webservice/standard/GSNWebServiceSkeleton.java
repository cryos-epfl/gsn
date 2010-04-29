/**
 * GSNWebServiceSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4.1  Built on : Aug 13, 2008 (05:03:35 LKT)
 */
package gsn.webservice.standard;

import gsn.Main;
import gsn.Mappings;
import gsn.beans.*;
import gsn.http.datarequest.AbstractQuery;
import gsn.http.datarequest.LimitCriterion;
import gsn.http.datarequest.QueriesBuilder;
import gsn.http.datarequest.xsd.AggregationCriterion;
import gsn.http.datarequest.xsd.StandardCriterion;
import gsn.storage.DataEnumerator;
import gsn.storage.StorageManager;
import gsn.webservice.standard.xsd.*;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.context.ServiceContext;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.util.*;

/**
 * GSNWebServiceSkeleton java skeleton for the axisService
 */
public class GSNWebServiceSkeleton {

    private static transient Logger logger = Logger.getLogger(GSNWebServiceSkeleton.class);


    /**
     * Auto generated method signature
     *
     * @param getVirtualSensorsDetails
     */

    public gsn.webservice.standard.GetVirtualSensorsDetailsResponse getVirtualSensorsDetails
            (
                    gsn.webservice.standard.GetVirtualSensorsDetails getVirtualSensorsDetails
            ) {
        //TODO : fill this with the necessary business logic
        throw new java.lang.UnsupportedOperationException("Please implement " + this.getClass().getName() + "#getVirtualSensorsDetails");
    }


    /**
     * Auto generated method signature
     */

    public gsn.webservice.standard.ListWrapperURLsResponse listWrapperURLs() {
        //throw new java.lang.UnsupportedOperationException("Please implement " + this.getClass().getName() + "#listWrapperURLs");
        ListWrapperURLsResponse response = new ListWrapperURLsResponse();
        Iterator<VSensorConfig> iter = Mappings.getAllVSensorConfigs();
        VSensorConfig config;
        ArrayList<String> wrappers = new ArrayList<String>();
        while (iter.hasNext()) {
            config = iter.next();
            for (gsn.beans.InputStream is : config.getInputStreams()) {
                for (gsn.beans.StreamSource source : is.getSources()) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(config.getName());
                    sb.append("/");
                    sb.append(is.getInputStreamName());
                    sb.append("/");
                    sb.append(source.getAlias());
                    sb.append("/");
                    sb.append(source.getActiveAddressBean().getWrapper());
                    wrappers.add(sb.toString());
                }
            }
        }
        response.setWrapperUrl(wrappers.toArray(new String[wrappers.size()]));
        return response;
    }


    /**
     * Auto generated method signature
     *
     * @param getLatestMultiData
     */

    public gsn.webservice.standard.GetLatestMultiDataResponse getLatestMultiData
            (
                    gsn.webservice.standard.GetLatestMultiData getLatestMultiData
            ) {
        //TODO : fill this with the necessary business logic
        throw new java.lang.UnsupportedOperationException("Please implement " + this.getClass().getName() + "#getLatestMultiData");
    }


    /**
     * Auto generated method signature
     *
     * @param unregisterQuery
     */

    public void unregisterQuery
            (
                    gsn.webservice.standard.UnregisterQuery unregisterQuery
            ) {
        //TODO : fill this with the necessary business logic

    }


    /**
     * Auto generated method signature
     *
     * @param createVirtualSensor
     */

    public void createVirtualSensor
            (
                    gsn.webservice.standard.CreateVirtualSensor createVirtualSensor
            ) {
        //TODO : fill this with the necessary business logic

    }


    /**
     * Auto generated method signature
     *
     * @param getNextData
     */

    public gsn.webservice.standard.GetNextDataResponse getNextData(gsn.webservice.standard.GetNextData getNextData) {
        //throw new java.lang.UnsupportedOperationException("Please implement " + this.getClass().getName() + "#getNextData");
        GetNextDataResponse response = new GetNextDataResponse();
        //
        QuerySession session = getSession(getNextData.getSid());
        if (session != null) {
            GSNWebService_QueryResult result = getResult(session);
            //
            response.addQueryResult(result);
        } else
            throw new IllegalArgumentException("The session '" + getNextData.getSid() + "' does not exist or is closed.");
        //
        return response;
    }


    /**
     * Auto generated method signature
     *
     * @param getMultiData
     */

    public gsn.webservice.standard.GetMultiDataResponse getMultiData(gsn.webservice.standard.GetMultiData getMultiData) {

        //throw new java.lang.UnsupportedOperationException("Please implement " + this.getClass().getName() + "#getMultiData");
        GetMultiDataResponse response = new GetMultiDataResponse();
        //

        Map<String, String[]> requestParameters = new HashMap<String, String[]>();

        // Build Mappings
        HashMap<String, ArrayList<String>> vsToField = new HashMap<String, ArrayList<String>>();
        HashMap<String, ArrayList<String>> fieldToVs = new HashMap<String, ArrayList<String>>();
        Iterator<VSensorConfig> iter = Mappings.getAllVSensorConfigs();
        while (iter.hasNext()) {
            VSensorConfig vsConfig = iter.next();
            ArrayList<String> fields = new ArrayList<String>();
            for (gsn.beans.DataField df : vsConfig.getOutputStructure()) {
                ArrayList<String> vss = fieldToVs.get(df.getName());
                if (vss == null) {
                    vss = new ArrayList<String>();
                    fieldToVs.put(df.getName(), vss);
                }
                vss.add(vsConfig.getName());
                fields.add(df.getName());
            }
            vsToField.put(vsConfig.getName(), fields);
        }

        // virtual sensor and field selection

        HashMap<String, ArrayList<String>> vsAndFields = new HashMap<String, ArrayList<String>>();
        GSNWebService_FieldSelector[] fieldSelectors = getMultiData.getFieldSelector();
        for (GSNWebService_FieldSelector fs : fieldSelectors) {
            String[] fields = fs.getFieldNames();
            // 1. Virtual Sensor Selection
            if ("ALL".equalsIgnoreCase(fs.getVsname())) {
                // 2. Fields Selection for the current Virtual Sensor Selection
                if (fields == null) {
                    // We select all the fields for all the virtual sensors
                    for (Map.Entry<String, ArrayList<String>> entry : vsToField.entrySet()) {
                        updateSelectionKey(vsAndFields, entry.getKey(), entry.getValue());
                    }
                } else {
                    // We select the specified fields (if they exist) for all the virtual sensors
                    for (String field : fields) {
                        ArrayList<String> _vss = fieldToVs.get(field);
                        if (_vss != null)
                            updateSelectionValue(vsAndFields, _vss, field);
                    }
                }
            } else {
                // 2. Fields Selection for the current Virtual Sensor Selection
                ArrayList<String> _fields = vsToField.get(fs.getVsname());
                if (_fields != null) {
                    // The virtual sensor exists
                    if (fields == null) {
                        // We select all the fields for the specified virtual sensor
                        updateSelectionKey(vsAndFields, fs.getVsname(), _fields);
                    } else {
                        // We select the specified fields (if they exist) for the specified virtual sensor
                        for (String field : fields) {
                            if (_fields.contains(field))
                                updateSelection(vsAndFields, fs.getVsname(), field);
                        }
                    }
                }
            }
        }

        ArrayList<String> vsnames = new ArrayList<String>();
        for (Map.Entry<String, ArrayList<String>> entry : vsAndFields.entrySet()) {
            StringBuilder sb = new StringBuilder();
            sb.append(entry.getKey());
            for (String elt : entry.getValue()) {
                sb.append(":").append(elt);
            }
            vsnames.add(sb.toString());
        }
        requestParameters.put("vsname", vsnames.toArray(new String[]{}));

        // time format

        String timeFormat = getMultiData.getTimeFormat();
        if (timeFormat != null)
            requestParameters.put("timeformat", new String[]{timeFormat});

        ArrayList<String> critFields = new ArrayList<String>();

        //from / to
        long from = getMultiData.getFrom();
        long to = getMultiData.getTo();
        for (String vsname : vsAndFields.keySet()) {
            if (from != java.lang.Long.MIN_VALUE)
                critFields.add("and::" + vsname + ":timed:ge:" + from);
            if (to != java.lang.Long.MIN_VALUE)
                critFields.add("and::" + vsname + ":timed:leq:" + to);
        }

        // conditions

        StandardCriterion[] standardCriteria = getMultiData.getConditions();
        if (standardCriteria != null) {
            for (StandardCriterion criterion : standardCriteria) {
                HashMap<String, ArrayList<String>> selection = new HashMap<String, ArrayList<String>>();
                if ("ALL".equalsIgnoreCase(criterion.getVsname())) {
                    if ("ALL".equalsIgnoreCase(criterion.getField())) {
                        // We add this criterion for all the virtual sensors and all their fields
                        selection = vsAndFields;
                    } else {
                        ArrayList<String> vss = fieldToVs.get(criterion.getField());
                        ArrayList<String> crit = new ArrayList<String>();
                        crit.add(criterion.getField());
                        if (vss != null) {
                            for (String vsname : vss) {
                                if (vsAndFields.containsKey(vsname))
                                    selection.put(vsname, crit);
                            }
                        }
                    }
                } else {
                    ArrayList<String> _fields = vsAndFields.get(criterion.getVsname());
                    if (_fields != null) {
                        if ("ALL".equalsIgnoreCase(criterion.getField())) {
                            selection.put(criterion.getVsname(), _fields);
                        } else {
                            if (_fields.contains(criterion.getField())) {
                                ArrayList<String> values = new ArrayList<String>();
                                values.add(criterion.getField());
                                selection.put(criterion.getVsname(), values);
                            }
                        }
                    }
                }
                for (Map.Entry<String, ArrayList<String>> entry : selection.entrySet()) {
                    String vsname = entry.getKey();
                    for (String field : entry.getValue()) {
                        //<critJoin>:<negation>:<vsname>:<field>:<operator>:<value>
                        StringBuilder sb = new StringBuilder();
                        sb.append(criterion.getCritJoin());
                        sb.append(":");
                        sb.append(criterion.getNegation());
                        sb.append(":");
                        sb.append(vsname);
                        sb.append(":");
                        sb.append(field);
                        sb.append(":");
                        sb.append(criterion.getOperator());
                        sb.append(":");
                        sb.append(criterion.getValue());
                        //
                        critFields.add(sb.toString());
                    }
                }
            }
        }

        requestParameters.put("critfield", critFields.toArray(new String[]{}));


        // nb

        /*long nb = getMultiData.getNb();
        if (nb != java.lang.Long.MIN_VALUE) // check if nb is set
            requestParameters.put("nb", new String[]{"0:" + nb});
        */
        int userNb = getMultiData.getNb();

        // aggregation

        AggregationCriterion aggregation = getMultiData.getAggregation();
        if (aggregation != null) {
            aggregation.getTimeRange();
            requestParameters.put("groupby", new String[]{new StringBuilder()
                    .append(aggregation.getTimeRange())
                    .append(":")
                    .append(aggregation.getGroupOperator()).toString()});
        }

        //
        try {
            QueriesBuilder qbuilder = new QueriesBuilder(requestParameters);
            for (Map.Entry<String, AbstractQuery> entry : qbuilder.getSqlQueries().entrySet()) {
                //
                QuerySession session = generateSession(entry.getValue(), entry.getKey(), userNb);
                //
                GSNWebService_QueryResult result = getResult(session);
                //
                response.addQueryResult(result);
            }
        }
        catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }

        //
        return response;
    }


    private GSNWebService_QueryResult getResult(QuerySession session) {
        GSNWebService_QueryResult result = new GSNWebService_QueryResult();
        result.setSid(session.sid);
        result.setVsname(session.vsname);
        DataEnumerator de = null;
        try {

            //
            LimitCriterion limit = new LimitCriterion();
            int offset = session.pageIndex * PAGE_SIZE;
            int remaining = Math.max(0, session.userNb - offset);
            limit.setOffset(0);
            limit.setSize(Math.min((PAGE_SIZE + 1), remaining));

            session.query.setLimitCriterion(limit);

            //
            gsn.http.datarequest.StandardCriterion stc = new gsn.http.datarequest.StandardCriterion();
            stc.setCritField("pk");
            stc.setCritJoin("AND");
            stc.setCritNeg("");
            stc.setCritOperator("<");
            stc.setCritValue(String.valueOf(session.lastPk));
            stc.setCritVsname(session.vsname);
            session.query.updateCriterion(stc);

            //
            Connection connection = StorageManager.getInstance().getConnection();
            de = StorageManager.getInstance().streamedExecuteQuery(session.query, false, connection);
            int page = 0;
            while (de.hasMoreElements() && page < PAGE_SIZE) {
                StreamElement se = de.nextElement();
                session.lastPk = se.getInternalPrimayKey();
                // Set the Format if needed
                if (result.getFormat() == null) {
                    GSNWebService_StreamElement gse = new GSNWebService_StreamElement();
                    for (int i = 0; i < se.getData().length; i++) {
                        GSNWebService_DataField field = new GSNWebService_DataField();
                        field.setName(se.getFieldNames()[i]);
                        field.setType(DataTypes.TYPE_NAMES[se.getFieldTypes()[i]]);
                        field.setString("");
                        gse.addField(field);
                    }
                    result.setFormat(gse);
                }
                //
                GSNWebService_StreamElement gse = new GSNWebService_StreamElement();
                //gse.setTimed(String.valueOf(se.getTimeStamp()));
                gse.setTimed(new Date(se.getTimeStamp()).toString());
                for (String field : se.getFieldNames()) {
                    GSNWebService_DataField df = new GSNWebService_DataField();
                    df.setString(se.getData(field).toString());
                    gse.addField(df);
                }
                result.addStreamElements(gse);
                page++;
            }

            result.setHasNext(de.hasMoreElements());
            if (result.getHasNext())
                addSession(session);
            else
                removeSession(session.sid);
        }
        catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        finally {
            if (de != null)
                de.close();
        }
        //
        StringBuilder sb = new StringBuilder();
        sb.append(session.query.getStandardQuery());
        if (session.query.getLimitCriterion() != null) {
            sb.append("(");
            sb.append(session.query.getLimitCriterion());
            sb.append(")");
        }
        result.setExecutedQuery(sb.toString());
        //
        session.pageIndex++;
        //
        return result;
    }

    /**
     * Auto generated method signature
     *
     * @param registerQuery
     */

    public void registerQuery
            (
                    gsn.webservice.standard.RegisterQuery registerQuery
            ) {
        //TODO : fill this with the necessary business logic

    }


    /**
     * Auto generated method signature
     *
     * @param getVirtualSensorDetails
     */

    public gsn.webservice.standard.GetVirtualSensorDetailsResponse getVirtualSensorDetails
            (
                    gsn.webservice.standard.GetVirtualSensorDetails getVirtualSensorDetails
            ) {
        //TODO : fill this with the necessary business logic
        throw new java.lang.UnsupportedOperationException("Please implement " + this.getClass().getName() + "#getVirtualSensorDetails");
    }


    /**
     * Auto generated method signature
     */

    public gsn.webservice.standard.GetContainerInfoResponse getContainerInfo() {
        //throw new java.lang.UnsupportedOperationException("Please implement " + this.getClass().getName() + "#getContainerInfo");
        GetContainerInfoResponse response = new GetContainerInfoResponse();
        GSNWebService_ContainerDetails cd = new GSNWebService_ContainerDetails();
        //
        ContainerConfig cc = Main.getContainerConfig();
        if (cc.getWebAuthor() != null)
            cd.setAuthor(cc.getWebAuthor());
        if (cc.getWebDescription() != null)
            cd.setDescription(cc.getWebDescription());
        if (cc.getWebEmail() != null)
            cd.setEmail(cc.getWebEmail());
        if (cc.getWebName() != null)
            cd.setName(cc.getWebName());
        if (cc.getTimeFormat() != null)
            cd.setTimeFormat(cc.getTimeFormat());
        cd.setPort(cc.getContainerPort());
        //
        response.setContainerDetails(cd);
        return response;
    }


    /**
     * Auto generated method signature
     */

    public gsn.webservice.standard.ListVirtualSensorNamesResponse listVirtualSensorNames() {
        //throw new java.lang.UnsupportedOperationException("Please implement " + this.getClass().getName() + "#listVirtualSensorNames");
        ListVirtualSensorNamesResponse response = new ListVirtualSensorNamesResponse();
        ArrayList<String> vsnames = new ArrayList<String>();
        Iterator<VSensorConfig> iter = Mappings.getAllVSensorConfigs();
        VSensorConfig config;
        while (iter.hasNext()) {
            config = iter.next();
            vsnames.add(config.getName());
        }
        response.setVirtualSensorName(vsnames.toArray(new String[vsnames.size()]));
        return response;
    }


    /**
     * Auto generated method signature
     *
     * @param deleteVirtualSensor
     */

    public void deleteVirtualSensor
            (
                    gsn.webservice.standard.DeleteVirtualSensor deleteVirtualSensor
            ) {
        //TODO : fill this with the necessary business logic

    }


    // Utility methods

    private void updateSelectionKey(HashMap<String, ArrayList<String>> source, String key, ArrayList<String> values) {
        if (source == null || key == null || values == null)
            return;
        ArrayList<String> sv = source.get(key);
        if (sv == null) {
            source.put(key, values);
        } else {
            for (String value : values) {
                if (!sv.contains(value))
                    sv.add(value);
            }
        }
    }

    private void updateSelectionValue(HashMap<String, ArrayList<String>> source, ArrayList<String> keys, String value) {
        if (source == null || keys == null || value == null)
            return;
        for (String key : keys) {
            ArrayList<String> sv = source.get(key);
            if (sv == null) {
                ArrayList<String> values = new ArrayList<String>();
                values.add(value);
                source.put(key, values);
            } else {
                if (!sv.contains(value))
                    sv.add(value);
            }
        }
    }

    private void updateSelection(HashMap<String, ArrayList<String>> source, String key, String value) {
        if (source == null || key == null || value == null)
            return;
        ArrayList<String> sv = source.get(key);
        if (sv == null) {
            ArrayList<String> values = new ArrayList<String>();
            values.add(value);
            source.put(key, values);
        } else {
            if (!sv.contains(value))
                sv.add(value);
        }
    }

    // Session management

    private static final int PAGE_SIZE = 1000;

    private static final String REQ_NB = "requestnb";

    private static final String SESSIONS = "sessions";

    private static final int INTERVAL_BETWEEN_STALE_SESION_GC = 100;

    private static final long MAX_IDLE_TIME = 5 * 60 * 1000; // 5 minutes

    public void init(ServiceContext serviceContext) {
        // Check if the scope is "application"
        if (!"application".equals(serviceContext.getAxisService().getScope())) {
            logger.error("The Service scope MUST be set to 'application' in the services.xml file.");
            return;
        }
        serviceContext.setProperty(REQ_NB, new Integer(0));
        serviceContext.setProperty(SESSIONS, new HashMap<String, QuerySession>());
    }

    public void destroy(ServiceContext serviceContext) {
        //
    }

    private QuerySession getSession(String sid) {
        ServiceContext serviceContext = MessageContext.getCurrentMessageContext().getServiceContext();
        HashMap<String, QuerySession> sessions = (HashMap<String, QuerySession>) serviceContext.getProperty(SESSIONS);
        //
        gcStaleSessions();
        //
        QuerySession session = sessions.get(sid);
        if (session != null) {
            session.lastAccessTime = System.currentTimeMillis();
            sessions.put(sid, session);
        }
        return session;
    }

    private void removeSession(String sid) {
        ServiceContext serviceContext = MessageContext.getCurrentMessageContext().getServiceContext();
        HashMap<String, QuerySession> sessions = (HashMap<String, QuerySession>) serviceContext.getProperty(SESSIONS);
        //
        sessions.remove(sid);
    }

    private void addSession(QuerySession session) {
        ServiceContext serviceContext = MessageContext.getCurrentMessageContext().getServiceContext();
        HashMap<String, QuerySession> sessions = (HashMap<String, QuerySession>) serviceContext.getProperty(SESSIONS);
        //
        gcStaleSessions();
        //
        if (!sessions.containsKey(session.sid)) {
            sessions.put(session.sid, session);
            serviceContext.setProperty(SESSIONS, sessions);
        }
    }

    private QuerySession generateSession(AbstractQuery query, String vsname, int nb) {
        QuerySession session = new QuerySession();
        session.vsname = vsname;
        session.sid = UUID.randomUUID().toString();
        session.lastAccessTime = System.currentTimeMillis();
        session.pageIndex = 0;
        //
        query.addField("pk");
        //
        session.query = query;
        session.userNb = nb;
        session.lastPk = Long.MAX_VALUE;
        return session;
    }

    private void gcStaleSessions() {
        ServiceContext serviceContext = MessageContext.getCurrentMessageContext().getServiceContext();
        HashMap<String, QuerySession> sessions = (HashMap<String, QuerySession>) serviceContext.getProperty(SESSIONS);
        // Gc Stale sessions
        Integer reqNb = (Integer) serviceContext.getProperty(REQ_NB);
        reqNb++;
        serviceContext.setProperty(REQ_NB, reqNb);
        Long currentTime = System.currentTimeMillis();
        //
        if (reqNb % INTERVAL_BETWEEN_STALE_SESION_GC == 0) {
            Iterator<Map.Entry<String, QuerySession>> iter = sessions.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, QuerySession> entry = iter.next();
                if (entry.getValue().lastAccessTime + MAX_IDLE_TIME < currentTime)
                    iter.remove();
            }
            serviceContext.setProperty(SESSIONS, sessions);
            logger.debug("Nb Of req: " + reqNb + " Nb of sessions: " + sessions.size());
        }
    }

    private class QuerySession {
        public String sid;
        public String vsname;
        public long lastPk;
        public int userNb;
        public int pageIndex;
        public AbstractQuery query;
        public long lastAccessTime;
    }
}
    