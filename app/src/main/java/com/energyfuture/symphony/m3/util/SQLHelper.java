package com.energyfuture.symphony.m3.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * ��ʼ����ݿ�
 *
 * @author �Ϻ�������Դ�Ƽ����޹�˾
 * @version 1.0
 * @designer
 * @since 2014-11-6
 */
public class SQLHelper extends SQLiteOpenHelper     {

    //ָ������db��ݿ����
    private static final String DATABASE_NAME = "symp.db";
    //��ǰ��ݿ�汾
    private static final int DATABASE_VERSION = 7;

    public static int Order = 0;
    private Context context;

    SQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
    }
        public static String TR_PROJECT = "TR_PROJECT";
        public static String SYMP_MESSAGE	= "SYMP_MESSAGE"; //ϵͳ��Ϣͬ����¼��ʷ��
        public static String SYMP_MESSAGE_PERSON = "SYMP_MESSAGE_PERSON"; //	��Ϣ������Ա��
        public static String SYMP_MESSAGE_REAL = "SYMP_MESSAGE_PERSON"; //	ϵͳ��Ϣͬ����¼ʵʱ��
        public static String TR_CONDITION_INFO = "TR_CONDITION_INFO"; //	������Ϣ�ɼ���¼��; InnoDB free: 37888 kB
        public static String TR_DATA_SYNCHRONIZATION = "TR_DATA_SYNCHRONIZATION"; //	��ݰ汾ͬ��
        public static String TR_DETECTION_TEMPLET	= "TR_DETECTION_TEMPLET"; //	�����Ŀģ��; InnoDB free: 37888 kB
        public static String TR_DETECTION_TOOLS = "TR_DETECTION_TOOLS"; //	����豸�����߱�; InnoDB free: 37888 kB
        public static String TR_DETECTIONTEMPLET_DATA_OBJ	= "TR_DETECTIONTEMPLET_DATA_OBJ"; //	�����Ŀģ�壨�����ʵ���; InnoDB free: 37888 kB
        public static String TR_DETECTIONTEMPLET_FILE_OBJ	= "TR_DETECTIONTEMPLET_FILE_OBJ"; //	�ļ�ģ��ʵ���; InnoDB free: 37888 kB
        public static String TR_DETECTIONTEMPLET_OBJ = "TR_DETECTIONTEMPLET_OBJ"; //	�����Ŀģ��ʵ���; InnoDB free: 37888 kB
        public static String TR_DETECTIONTYP_ETEMPLET_OBJ	= "TR_DETECTIONTYP_ETEMPLET_OBJ"; //	��ż����Ŀ����ģ��Ĺ�����ϵʵ���; InnoDB free: 37888 kB
        public static String TR_EQUIPMENT	= "TR_EQUIPMENT"; //	����豸��; InnoDB free: 37888 kB
        public static String TR_EQUIPMENT_TOOLS = "TR_EQUIPMENT_TOOLS"; //	��¼����������Я��Ĺ���
        public static String TR_LOG = "TR_LOG"; //	ϵͳ��־��; InnoDB free: 37888 kB
        public static String TR_SAFETYTIPS_OBJ = "TR_SAFETYTIPS_OBJ"; //	��ȫ��֪ʵ���; InnoDB free: 37888 kB
        public static String TR_SPECIAL_BUSHING = "TR_SPECIAL_BUSHING"; //	�����Ŀ���׹ܣ�; InnoDB free: 37888 kB
        public static String TR_SPECIAL_BUSHING_DATA = "TR_SPECIAL_BUSHING_DATA"; //	�����Ŀ���׹ܣ���ݱ�; InnoDB free: 37888 kB
        public static String TR_SPECIAL_BUSHING_FILE = "TR_SPECIAL_BUSHING_FILE"; //	�׹��ļ���; InnoDB free: 37888 kB
        public static String TR_SPECIAL_BUSHING_POSITION = "TR_SPECIAL_BUSHING_POSITION"; //	�����Ŀ���׹ܣ����λ��; InnoDB free: 37888 kB
        public static String TR_SPECIAL_OLI_DATA = "TR_SPECIAL_OLI_DATA"; //	�����Ŀ����ɫ�ף���ݱ�
        public static String TR_SPECIAL_WORK = "TR_SPECIAL_WORK"; //	�����Ŀ-���������¼
        public static String TR_SYSTEMCODE = "TR_SYSTEMCODE"; //	ϵͳ�����; InnoDB free: 37888 kB
        public static String TR_SYSTEMCODETYPE = "TR_SYSTEMCODETYPE"; //	ϵͳ�������ͱ�; InnoDB free: 37888 kB
        public static String TR_TASK = "TR_TASK"; //	������Ϣ��
        public static String USER_INFO = "USER_INFO";
        public static String TR_NOISE_RECORD = "TR_NOISE_RECORD";//��ѹ�������������¼
        public static String TR_NOISE_RECORD_GROUP = "TR_NOISE_RECORD_GROUP";//��ѹ�������������¼����
        public static String TR_NOISE_RECORD_VALUE = "TR_NOISE_RECORD_VALUE";//���������ֵ
        public static String SYMP_COMMUNICATION_THEME = "SYMP_COMMUNICATION_THEME";//��ͨ��������
        public static String SYMP_COMMUNICATION_REPLY = "SYMP_COMMUNICATION_REPLY";//��ͨ�����ظ�
        public static String SYMP_COMMUNICATION_PROMPT = "SYMP_COMMUNICATION_PROMPT";//��ͨ��������
        public static String SYMP_COMMUNICATION_ACCESSORY = "SYMP_COMMUNICATION_ACCESSORY";//��ͨ��������
        public static String TR_REPORT = "TR_REPORT";//��ͨ��������
        public static String TR_COMMONTOOLS = "TR_COMMONTOOLS";//��ͨ��������
        public static String TR_PARAMETER = "TR_PARAMETER";
        public static String TR_FEEDBACK_THEME = "TR_FEEDBACK_THEME";
        public static String TR_FEEDBACK_REPLY = "TR_FEEDBACK_REPLY";
        public static String TR_FEEDBACK_ACCESSORY = "TR_FEEDBACK_ACCESSORY";

        public static Map<String, String> TableKeyMap = new HashMap<String, String>();

        /**
         * �����ݱ�������ݱ������
         * @param tableName
         * @return
         */
        public static String getTableKey(String tableName){
                if(TableKeyMap.size() == 0){
                        //����ÿ�ű������
                        TableKeyMap.put(TR_PROJECT,"PROJECTID");
                        TableKeyMap.put(SYMP_MESSAGE,"MESSAGEID");
                        TableKeyMap.put(SYMP_MESSAGE_PERSON,"ID");
                        TableKeyMap.put(SYMP_MESSAGE_REAL,"MESSAGEID");
                        TableKeyMap.put(TR_CONDITION_INFO,"RECORDID");
                        TableKeyMap.put(TR_DATA_SYNCHRONIZATION,"SYNCHRONIZATIONID");
                        TableKeyMap.put(TR_DETECTION_TEMPLET,"ID");
                        TableKeyMap.put(TR_DETECTION_TOOLS,"ID");
                        TableKeyMap.put(TR_DETECTIONTEMPLET_DATA_OBJ,"ID");
                        TableKeyMap.put(TR_DETECTIONTEMPLET_FILE_OBJ,"ID");
                        TableKeyMap.put(TR_DETECTIONTEMPLET_OBJ,"ID");
                        TableKeyMap.put(TR_DETECTIONTYP_ETEMPLET_OBJ,"ID");
                        TableKeyMap.put(TR_EQUIPMENT,"EQUIPMENTID");
                        TableKeyMap.put(TR_EQUIPMENT_TOOLS,"ID");
                        TableKeyMap.put(TR_LOG,"LOGID");
                        TableKeyMap.put(TR_SAFETYTIPS_OBJ,"ID");
                        TableKeyMap.put(TR_SPECIAL_BUSHING,"ID");
                        TableKeyMap.put(TR_SPECIAL_BUSHING_DATA,"ID");
                        TableKeyMap.put(TR_SPECIAL_BUSHING_FILE,"ID");
                        TableKeyMap.put(TR_SPECIAL_BUSHING_POSITION,"ID");
                        TableKeyMap.put(TR_SPECIAL_OLI_DATA,"ID");
                        TableKeyMap.put(TR_SPECIAL_WORK,"ID");
                        TableKeyMap.put(TR_SYSTEMCODE,"ID");
                        TableKeyMap.put(TR_SYSTEMCODETYPE,"ID");
                        TableKeyMap.put(TR_TASK,"TASKID");
                        TableKeyMap.put(USER_INFO,"YHID");
                        TableKeyMap.put(TR_NOISE_RECORD,"ID");
                        TableKeyMap.put(TR_NOISE_RECORD_GROUP,"ID");
                        TableKeyMap.put(TR_NOISE_RECORD_VALUE,"ID");
                        TableKeyMap.put(SYMP_COMMUNICATION_THEME,"ID");
                        TableKeyMap.put(SYMP_COMMUNICATION_REPLY,"ID");
                        TableKeyMap.put(SYMP_COMMUNICATION_PROMPT,"ID");
                        TableKeyMap.put(SYMP_COMMUNICATION_ACCESSORY,"ID");
                        TableKeyMap.put(TR_REPORT,"ID");
                        TableKeyMap.put(TR_COMMONTOOLS,"ID");
                        TableKeyMap.put(TR_PARAMETER,"ID");
                        TableKeyMap.put(TR_FEEDBACK_THEME,"ID");
                        TableKeyMap.put(TR_FEEDBACK_REPLY,"ID");
                        TableKeyMap.put(TR_FEEDBACK_ACCESSORY,"ID");
                }
                return TableKeyMap.get(tableName);
        }

    /**
     * ��ʼ��������ݿ�
     *
     * @param
     * @return void
     * @designer
     * @author zhaogr
     * @since 2014-11-6
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //��Ŀ��Ϣ��
        db.execSQL("CREATE TABLE tr_project (" +
                "  PROJECTID varchar(36) NOT NULL PRIMARY KEY," +
                "  PROJECTNAME varchar(50) default NULL," +
                "  PROJECTSTATE char(1) default NULL," +
                "  STATIONNAME varchar(50) default NULL," +
                "  STATIONID varchar(36) default NULL," +
                "  STARTTIME DATE NOT NULL default '0000-00-00 00:00:00'," +
                "  ENDTIME DATE NOT NULL default '0000-00-00 00:00:00'," +
                "  ACTUALSTARTTIME DATE NOT NULL default '0000-00-00 00:00:00'," +
                "  ACTUALSENDTIME DATE NOT NULL default '0000-00-00 00:00:00'," +
                "  CREATETIME DATE NOT NULL default '0000-00-00 00:00:00'," +
                "  AWARE varchar(300) default NULL," +
                "  ZPPERSON varchar(36) default NULL," +
                "  CREATEPERSON varchar(36) default NULL," +
                "  ZRPEARSON varchar(36) default NULL," +
                "  DEPARTMENT varchar(30)," +
                "  WORKPERSON varchar(100) default NULL," +
                "  UPDATETIME DATE NOT NULL default '0000-00-00 00:00:00'," +
                "  REASONDESCRIBE varchar(300) default NULL," +
                "  SAFETYNOTICE varchar(300) default NULL," +
                "  TASKCOUNT int(11) default NULL," +
                "  FINISHCOUNT int(11) default NULL" +
                ")");
        //�û���
        db.execSQL("CREATE TABLE user_info (" +
                "  YHID varchar(70) NOT NULL PRIMARY KEY," +
                "  BMBH varchar(70) default NULL," +
                "  XM varchar(32) default NULL," +
                "  YHMM varchar(32) default NULL," +
                "  SFZH varchar(18) default NULL," +
                "  YHZW varchar(20) default NULL," +
                "  EMAIL varchar(64) default NULL," +
                "  SJHM varchar(15) default NULL," +
                "  YHZT char(1) default NULL," +
                "  MJ decimal(5,0) default NULL," +
                "  CJR varchar(18) default NULL," +
                "  CJSJ varchar(14) default NULL," +
                "  XGR varchar(18) default NULL," +
                "  XGSJ varchar(14) default NULL," +
                "  SSXT varchar(100) default NULL," +
                "  UPDATETIME timestamp NULL default NULL" +
                ")");
        //Ȩ����Դ����
        db.execSQL("CREATE TABLE auth_res_type (" +
                "  QXZYLB varchar(70) NOT NULL PRIMARY KEY," +
                "  QXLBMS varchar(30) NOT NULL," +
                "  SFKY char(1) NOT NULL," +
                "  ZYSX varchar(10) default NULL," +
                "  YYXTDM varchar(4) default NULL," +
                "  SX decimal(5,0) default NULL" +
                ")");

        //�û�Ȩ�ޱ�
        db.execSQL("CREATE TABLE user_auth (" +
                "  YHID varchar(70) NOT NULL PRIMARY KEY," +
                "  JSID varchar(70) NOT NULL," +
                "  SQR varchar(70) NOT NULL," +
                "  SQSJ varchar(70) NOT NULL," +
                "  SPZT varchar(70) NOT NULL," +
                "  BPZYY varchar(70) default NULL," +
                "  MS varchar(70) default NULL," +
                "  SQLX varchar(70) default NULL" +
                ")");

        //������Ϣ��
        db.execSQL("CREATE TABLE tr_task (" +
                "  TASKID varchar(36) NOT NULL PRIMARY KEY," +
                "  PROJECTID varchar(32) default NULL," +
                "  EQUIPMENTID varchar(32) default NULL," +
                "  TASKNAME varchar(50) default NULL," +
                "  TASKSTATE varchar(1) default NULL," +
                "  PLANSTARTTIME timestamp NOT NULL default CURRENT_TIMESTAMP," +
                "  PLANENDTIME timestamp NOT NULL default '0000-00-00 00:00:00'," +
                "  ACTUALSTARTTIME timestamp NOT NULL default '0000-00-00 00:00:00'," +
                "  ACTUALSENDTIME timestamp NOT NULL default '0000-00-00 00:00:00'," +
                "  PERSON varchar(36) default NULL," +
                "  TASKPERSON varchar(36) default NULL," +
                "  DETECTIONTTYPE varchar(36) default NULL," +
                "  UPDATETIME timestamp NOT NULL default '0000-00-00 00:00:00'," +
                "  FINISHTIME timestamp NOT NULL default '0000-00-00 00:00:00'," +
                "  STEPCOUNT int(11) default NULL," +
                "  FINISHCOUNT int(11) default NULL," +
                "  WORKPERSON varchar(100) default NULL," +
                "  ORDERBY int(11) default NULL," +
                "  APPRAISE_ABNORMAL varchar(200) default NULL," +
                "  APPRAISE_SUGGEST varchar(200) default NULL," +
                "  APPRAISE_CHANGEFLOW varchar(200) default NULL," +
                "  APPRAISE_CHANGEPERSON varchar(200) default NULL," +
                "  CHANGEENV varchar(200) default NULL," +
                "  CHANGEEQUIPMENT varchar(200) default NULL," +
                "  CHANGEWORK varchar(200) default NULL," +
                "  CHANGEOTHER varchar(200) default NULL," +
                "  IMPROVE varchar(200) default NULL," +
                "  METHOD varchar(200) default NULL," +
                "  RECOVERPERSON varchar(20) default NULL," +
                "  CLEANPERSON varchar(20) default NULL," +
                "  CONCLUSIONPERSON varchar(20) default NULL," +
                "  AUDIT_RESULT varchar(200) default NULL," +
                "  AUDIT_QUESTION varchar(200) default NULL," +
                "  AUDITPERSON varchar(20) default NULL" +
                ")");

        //ϵͳ�������ͱ�
        db.execSQL("CREATE TABLE tr_systemcodetype (" +
                "  ID varchar(36) NOT NULL PRIMARY KEY," +
                "  CODENAME varchar(50) default NULL," +
                "  CODEVALUE varchar(20) default NULL," +
                "  FORDER varchar(1) default NULL" +
                ")");

        //Ȩ����Դ��
        db.execSQL("CREATE TABLE auth_resource (" +
                "  QXID varchar(70) NOT NULL PRIMARY KEY," +
                "  QXZYLB varchar(70) NOT NULL," +
                "  QXZYBS varchar(70) default NULL," +
                "  QXZYMC varchar(64) NOT NULL," +
                "  YYXTDM varchar(4) default NULL," +
                "  SFKY char(1) NOT NULL," +
                "  SX decimal(5,0) default NULL," +
                "  QXZYURL varchar(600) default NULL," +
                "  PID varchar(70) default NULL," +
                "  SQLX char(1) default NULL," +
                "  SCYY varchar(160) default NULL," +
                "  CJR varchar(70) default NULL" +
                ")");

        //���ű�
        db.execSQL("CREATE TABLE department (" +
                "  BMID varchar(64) NOT NULL PRIMARY KEY," +
                "  BMMC varchar(64) NOT NULL," +
                "  SSBM varchar(64) default NULL," +
                "  BMLX varchar(64) default NULL" +
                ")");

        //��ɫȨ�ޱ�
        db.execSQL("CREATE TABLE role_auth (" +
                "  JSID varchar(70) NOT NULL PRIMARY KEY," +
                "  QXID varchar(70) NOT NULL," +
                "  SQR varchar(18) NOT NULL," +
                "  SQSJ varchar(14) NOT NULL," +
                "  SPZT varchar(1) NOT NULL," +
                "  SQLX varchar(1) NOT NULL" +
                ")");

        //��ɫ��
        db.execSQL("CREATE TABLE role_info (" +
                "  JSID varchar(70) NOT NULL PRIMARY KEY," +
                "  JSMC varchar(70) NOT NULL," +
                "  SFKY char(1) NOT NULL," +
                "  CJR varchar(18) NOT NULL," +
                "  CJSJ varchar(14) NOT NULL," +
                "  JGDM varchar(10) default NULL," +
                "  JSSX char(1) default NULL," +
                "  YYXTDM varchar(4) default NULL," +
                "  SQLX char(1) NOT NULL," +
                "  SCYY varchar(160) default NULL" +
                ")");

        //��Ϣ��
        db.execSQL("CREATE TABLE symp_message (" +
                "  MESSAGEID varchar(36) NOT NULL PRIMARY KEY," +
                "  PRESONID varchar(36) default NULL," +
                "  CONTENT text," +
                "  MESSAGESTATE char(1) default NULL," +
                "  MESSAGETYPE varchar(10) default NULL," +
                "  FCOMMENT varchar(200) default NULL," +
                "  CREATEDATE timestamp NOT NULL default CURRENT_TIMESTAMP," +
                "  SENDDATE timestamp NULL default '0000-00-00 00:00:00'" +
                ")");

        //��Ϣ������Ա��
        db.execSQL("CREATE TABLE symp_message_person (" +
                "  ID varchar(36) NOT NULL PRIMARY KEY," +
                "  PERSONID varchar(36) NOT NULL," +
                "  PERSONNAME varchar(50) default NULL," +
                "  REMARKS varchar(100) default NULL," +
                "  RECORDTIME timestamp NOT NULL default CURRENT_TIMESTAMP" +
                ")");

        //��Ϣʵʱ��
        db.execSQL("CREATE TABLE symp_message_real (" +
                "  MESSAGEID varchar(36) NOT NULL PRIMARY KEY," +
                "  PRESONID varchar(36) default NULL," +
                "  CONTENT text," +
                "  MESSAGESTATE char(1) default '0'," +
                "  MESSAGETYPE varchar(10) default NULL," +
                "  FCOMMENT varchar(200) default NULL," +
                "  CREATEDATE timestamp NOT NULL default CURRENT_TIMESTAMP," +
                "  SENDDATE timestamp NULL default '0000-00-00 00:00:00'" +
                ")");

        //����ֱ�
        db.execSQL("CREATE TABLE tr_bureau (" +
                "  BUREAUID varchar(36) NOT NULL PRIMARY KEY," +
                "  BUREAUNAME varchar(64) default NULL," +
                "  LISTID char(1) default NULL," +
                "  BUREAUIMG varchar(60) default NULL," +
                "  POSITIONX float default NULL," +
                "  POSITIONY float default NULL," +
                "  PROVINCEID varchar(36) default NULL" +
                ")");

        //������
        db.execSQL("CREATE TABLE tr_condition_info (" +
                "  RECORDID varchar(36) NOT NULL PRIMARY KEY," +
                "  TASKID varchar(36) default NULL," +
                "  PERSONID varchar(36) default NULL," +
                "  RECORDDATE timestamp NOT NULL default CURRENT_TIMESTAMP," +
                "  TEMPERATURE float default NULL," +
                "  HUMIDITY float default NULL," +
                "  WEATHER char(10) default NULL," +
                "  ANEMOGRAPH float default NULL," +
                "  RADIATION float default NULL," +
                "  STATIONID varchar(36) default NULL" +
                ")");

        //����豸�����߱�
        db.execSQL("CREATE TABLE tr_detection_tools (" +
                "  ORDERNUMBER int(11) default NULL PRIMARY KEY," +
                "  ID varchar(36) NOT NULL," +
                "  TOOLSTYPE varchar(30) default NULL," +
                "  COMPANY varchar(100) default NULL," +
                "  MODEL varchar(50) default NULL," +
                "  EQUIPMENTNAME varchar(32) default NULL," +
                "  PRODUCTIONDATE timestamp NOT NULL default CURRENT_TIMESTAMP," +
                "  CCBH varchar(64) default NULL," +
                "  STATE varchar(1) default NULL," +
                "  VALIDDATE timestamp NULL default NULL," +
                "  DETECTIONTYPE varchar(10) default NULL," +
                "  UNIT varchar(10) default NULL," +
                "  CORRECTING varchar(50) default NULL," +
                "  CELLGROUP varchar(50) default NULL" +
                ")");

        //��Ŀģ���
        db.execSQL("CREATE TABLE tr_detection_templet (" +
                "  ID varchar(36) NOT NULL PRIMARY KEY," +
                "  DETECTIONMETHOD varchar(50) default NULL," +
                "  DETECTIONSTANDARD varchar(500) default NULL," +
                "  DETECTIONEXAMPLE varchar(100) default NULL," +
                "  REMARKS varchar(100) default NULL," +
                "  UPDATETIME timestamp NOT NULL default CURRENT_TIMESTAMP," +
                "  UPDATEPERSON varchar(50) default NULL," +
                "  TEMPLETTYPE varchar(50) default NULL" +
                ")");

        //��Ŀģ��?�����
        db.execSQL("CREATE TABLE tr_detectiontemplet_data (" +
                "  ID varchar(36) NOT NULL PRIMARY KEY," +
                "  TEMPLETID varchar(36) default NULL," +
                "  REQUIRED varchar(1) default NULL," +
                "  DATAFORMAT varchar(20) default NULL," +
                "  DATAUNIT varchar(20) default NULL," +
                "  DESCRIBETION varchar(100) default NULL," +
                "  ORDERMUNBER int(11) default NULL," +
                "  UPDATETIME timestamp NOT NULL default CURRENT_TIMESTAMP," +
                "  UPDATEPERSON varchar(50) default NULL" +
                ")");

        //��Ŀģ��ʵ��?�����
        db.execSQL("CREATE TABLE tr_detectiontemplet_data_obj (" +
                "  ID varchar(36) NOT NULL PRIMARY KEY," +
                "  DETECTIONOBJID varchar(36) default NULL," +
                "  REQUIRED varchar(1) default NULL," +
                "  DATAFORMAT varchar(20) default NULL," +
                "  DATAUNIT varchar(20) default NULL," +
                "  DESCRIBETION varchar(100) default NULL," +
                "  ORDERMUNBER int(11) default NULL," +
                "  RESULT varchar(50) default NULL," +
                "  RESULTDESCRIBE varchar(200) default NULL," +
                "  UPDATETIME timestamp NOT NULL default CURRENT_TIMESTAMP," +
                "  UPDATEPERSON varchar(50) default NULL" +
                ")");

        //�ļ�ģ���
        db.execSQL("CREATE TABLE tr_detectiontemplet_file (" +
                "  ID varchar(36) NOT NULL PRIMARY KEY," +
                "  TEMPLETID varchar(36) default NULL," +
                "  FILETYPE varchar(50) default NULL," +
                "  DESCRIBETION varchar(200) default NULL," +
                "  REQUIRED varchar(1) default NULL," +
                "  UPDATETIME timestamp NOT NULL default CURRENT_TIMESTAMP," +
                "  UPDATEPERSON varchar(50) default NULL" +
                ")");

        //�ļ�ģ��ʵ���
        db.execSQL("CREATE TABLE tr_detectiontemplet_file_obj (" +
                "  ID varchar(36) NOT NULL PRIMARY KEY," +
                "  DETECTIONOBJID varchar(36) default NULL," +
                "  TEMPLETNAME varchar(50) default NULL," +
                "  TEMPLETTYPE varchar(50) default NULL," +
                "  DESCRIBETION varchar(200) default NULL," +
                "  REQUIRED varchar(1) default NULL," +
                "  FILENUMBER varchar(50) default NULL," +
                "  FILENAME varchar(50) default NULL," +
                "  FILEURL varchar(200) default NULL," +
                "  UPDATETIME timestamp NOT NULL default CURRENT_TIMESTAMP," +
                "  UPDATEPERSON varchar(50) default NULL," +
                "  PROJECTID varchar(36) default NULL," +
                "  TASKID varchar(36) default NULL," +
                "  ORDERBY int(11) default NULL," +
                "  SIGNID varchar(36) default NULL," +
                "  ISUPLOAD varchar(1) default NULL" +
                ")");

        //��Ŀģ��ʵ���
        db.execSQL("CREATE TABLE tr_detectiontemplet_obj (" +
                "  ID varchar(36) NOT NULL PRIMARY KEY," +
                "  DETECTIONDES varchar(50) default NULL," +
                "  DETECTIONMETHOD varchar(50) default NULL," +
                "  DETECTIONSTANDARD varchar(500) default NULL," +
                "  DETECTIONEXAMPLE varchar(100) default NULL," +
                "  STATUS varchar(1) default NULL," +
                "  RESULTDESCRIBE varchar(200) default NULL," +
                "  REMARKS varchar(200) default NULL," +
                "  UPDATETIME timestamp NOT NULL default CURRENT_TIMESTAMP," +
                "  ORDERBY int default NULL," +
                "  UPDATEPERSON varchar(50) default NULL" +
                ")");

        //�����Ŀ����ģ��Ĺ�����ϵʵ���
        db.execSQL("CREATE TABLE tr_detectiontyp_etemplet_obj (" +
                "  ID varchar(36) NOT NULL PRIMARY KEY," +
                "  RID varchar(36) default NULL," +
                "  TASKID varchar(36) default NULL," +
                "  DETECTIONNAME varchar(50) NOT NULL," +
                "  PID varchar(36) default NULL," +
                "  DETECTIONTEMPLET varchar(32) default NULL," +
                "  TEMPLETTYPE varchar(50)," +
                "  ORDERBY int default NULL," +
                "  PICCOUNT int default NULL," +
                "  STATUS varchar(10) default NULL" +
                ")");

        //�����Ŀ����ģ�����ģ���
        db.execSQL("CREATE TABLE tr_detectiontype_templet (" +
                "  ID varchar(36) NOT NULL PRIMARY KEY," +
                "  DETECTIONNAME varchar(50) NOT NULL," +
                "  PID varchar(36) default NULL," +
                "  DETECTIONTEMPLETID varchar(36) default NULL," +
                "  PICCOUNT int default NULL," +
                "  UPDATETIME timestamp NOT NULL default CURRENT_TIMESTAMP," +
                "  UPDATEPERSON varchar(50) default NULL" +
                ")");

        //����豸��
        db.execSQL("CREATE TABLE tr_equipment (" +
                "  EQUIPMENTID varchar(36) NOT NULL PRIMARY KEY," +
                "  BUREAUNAME varchar(32) default NULL," +
                "  BUREAUID varchar(36) default NULL," +
                "  STATIONNAME varchar(60) default NULL," +
                "  STATIONID varchar(36) default NULL," +
                "  VOLTAGE varchar(50) default NULL," +
                "  EQUIPMENTTYPE varchar(10) default NULL," +
                "  COMPANY varchar(100) default NULL," +
                "  MODEL varchar(50) default NULL," +
                "  EQUIPMENTNAME varchar(32) default NULL," +
                "  PRODUCTIONDATE timestamp NOT NULL default CURRENT_TIMESTAMP," +
                "  USEDATE timestamp NOT NULL default '0000-00-00 00:00:00'," +
                "  XH int(11) default NULL," +
                "  EQUIPMENTIMG varchar(60) default NULL," +
                "  CCBH varchar(64) default NULL," +
                "  YBBH varchar(64) default NULL," +
                "  ORDERBY int(11) default NULL," +
                "  RATEDCAPACITY varchar(50) default NULL," +
                "  UPDATETIME timestamp NOT NULL default '0000-00-00 00:00:00'," +
                "  VOLTAGESETMODEL varchar(50) default NULL," +
                "  COOLMETHOD varchar(50) default NULL," +
                "  PHASECOUNT varchar(10) default NULL," +
                "  FREQUENCY varchar(20) default NULL," +
                "  CONNECTNUMBER varchar(20) default NULL," +
                "  USECONDITION varchar(20) default NULL," +
                "  OIL varchar(20) default NULL," +
                "  PHASE varchar(10) default NULL" +
                "  )");

        //ϵͳ��־��
        db.execSQL("CREATE TABLE tr_log (" +
                "  LOGID varchar(36) NOT NULL PRIMARY KEY," +
                "  USERID varchar(36) default NULL," +
                "  USERNAME varchar(36) default NULL," +
                "  CZXX varchar(100) default NULL," +
                "  CZDATE timestamp NOT NULL default CURRENT_TIMESTAMP," +
                "  CZTYPE varchar(10) default NULL," +
                "  CZSTATE char(1) default NULL" +
                ")");

        //��Ŀ��Ӧ������ͱ�
        db.execSQL("CREATE TABLE tr_project_equipment (" +
                "  RELATIONID varchar(32) NOT NULL PRIMARY KEY," +
                "  PROJECTID varchar(32) default NULL," +
                "  DETECTIONNAME varchar(32) default NULL," +
                "  ORDERBY varchar(10) default NULL" +
                ")");

        //ʡ��˾̨վ��
        db.execSQL("CREATE TABLE tr_province (" +
                "  PROVINCEID varchar(36) NOT NULL PRIMARY KEY," +
                "  PROVINCENAME varchar(64) default NULL" +
                ")");

        //��ȫ��֪ʵ���
        db.execSQL("CREATE TABLE tr_safetytips_obj (" +
                "  ID varchar(36) NOT NULL PRIMARY KEY," +
                "  ENDANGERNAME varchar(100) default NULL," +
                "  ENDANGERRANG varchar(1000) default NULL," +
                "  RiISKRANG varchar(100) default NULL," +
                "  RESULT varchar(100) default NULL," +
                "  GRADE varchar(50) default NULL," +
                "  PRECONTROLMETHOD varchar(500) default NULL," +
                "  METHOD varchar(100) default NULL," +
                "  CONTINUELEVEL varchar(100) default NULL," +
                "  REMARKS varchar(100) default NULL," +
                "  SAFETYPE varchar(30) default NULL," +
                "  TASKID varchar(36) default NULL," +
                "  ORDERMUNBER int(11) default NULL," +
                "  PERSION varchar(50) default NULL," +
                "  UPDATETIME timestamp NOT NULL default CURRENT_TIMESTAMP" +
                ")");

        //��ȫ��֪ģ���
        db.execSQL("CREATE TABLE tr_safetytips_templet (" +
                "  ID varchar(36) NOT NULL PRIMARY KEY," +
                "  ENDANGERNAME varchar(100) default NULL," +
                "  ENDANGERRANG varchar(1000) default NULL," +
                "  RiISKRANG varchar(100) default NULL," +
                "  RESULT varchar(100) default NULL," +
                "  GRADE varchar(50) default NULL," +
                "  PRECONTROLMETHOD varchar(500) default NULL," +
                "  METHOD varchar(100) default NULL," +
                "  CONTINUELEVEL varchar(100) default NULL," +
                "  REMARKS varchar(100) default NULL," +
                "  SAFETYPE varchar(30) default NULL," +
                "  ORDERMUNBER int(11) default NULL" +
                ")");

        //�����Ŀ�?�׹ܣ�
        db.execSQL("CREATE TABLE tr_special_bushing (" +
                "  ID varchar(36) NOT NULL PRIMARY KEY," +
                "  DETECTIONPROJECTID varchar(36) default NULL," +
                "  DETECTIONDES varchar(50) default NULL," +
                "  DETECTIONMETHOD varchar(50) default NULL," +
                "  DETECTIONSTANDARD varchar(500) default NULL," +
                "  DETECTIONEXAMPLE varchar(100) default NULL," +
                "  STATUS varchar(1) default NULL," +
                "  RESULTDESCRIBE varchar(200) default NULL," +
                "  REMARKS varchar(200) default NULL," +
                "  UPDATETIME timestamp NOT NULL default CURRENT_TIMESTAMP," +
                "  UPDATEPERSON varchar(50) default NULL" +
                ")");

        //�����Ŀ���׹ܣ���ݱ�
        db.execSQL("CREATE TABLE tr_special_bushing_data (" +
                "  ID varchar(36) NOT NULL PRIMARY KEY," +
                "  POSITIONID varchar(36) default NULL," +
                "  RESULT varchar(50) default NULL," +
                "  ORDERMUNBER int(11) default NULL," +
                "  DESCRIBETION varchar(200) default NULL," +
                "  UPDATETIME timestamp NOT NULL default CURRENT_TIMESTAMP," +
                "  UPDATEPERSON varchar(50) default NULL" +
                ")");

        //�׹��ļ���
        db.execSQL("CREATE TABLE tr_special_bushing_file (" +
                "  ID varchar(36) NOT NULL PRIMARY KEY," +
                "  POSITIONID varchar(36) default NULL," +
                "  REQUIRED varchar(1) default NULL," +
                "  FILENUMBER varchar(50) default NULL," +
                "  FILENAME varchar(50) default NULL," +
                "  FILEURL varchar(200) default NULL," +
                "  UPDATETIME timestamp NOT NULL default CURRENT_TIMESTAMP," +
                "  UPDATEPERSON varchar(50) default NULL," +
                "  PROJECTID varchar(36) default NULL," +
                "  TASKID varchar(36) default NULL," +
                "  ORDERBY int(11) default NULL," +
                "  ISUPLOAD varchar(1) default NULL" +
                ")");

        //�����Ŀ���׹ܣ����λ�ñ�
        db.execSQL("CREATE TABLE tr_special_bushing_position (" +
                "  ID varchar(32) NOT NULL PRIMARY KEY," +
                "  BUSHINGID varchar(32) default NULL," +
                "  VOLTAGE varchar(50) default NULL," +
                "  ORDERBY int default NULL," +
                "  PHASH varchar(50) default NULL" +
                ")");

        //���վ��
        db.execSQL("CREATE TABLE tr_station (" +
                "  STATIONID varchar(36) NOT NULL PRIMARY KEY," +
                "  BUREAUID varchar(36) default NULL," +
                "  VOLTAGE varchar(10) default NULL," +
                "  STATIONNAME varchar(64) default NULL," +
                "  SUBCODE varchar(32) default NULL," +
                "  VISIBLE varchar(1) default NULL," +
                "  POSITIONX float default NULL," +
                "  POSITIONY float default NULL" +
                ")");

        //ϵͳ�����
        db.execSQL("CREATE TABLE tr_systemcode (" +
                "  CODEID varchar(36) NOT NULL PRIMARY KEY," +
                "  CODENAME varchar(64) default NULL," +
                "  CODEVALUE varchar(64) default NULL," +
                "  CODETYPE varchar(32) default NULL," +
                "  FORDER int(11) default NULL," +
                "  PID varchar(64) default NULL," +
                "  ISLEAF varchar(1) default NULL" +
                ")");

        //�����빤�߹�ϵ��
        db.execSQL("CREATE TABLE tr_equipment_tools (" +
                "  ID varchar(36) NOT NULL PRIMARY KEY," +
                "  TASKID varchar(36) default NULL," +
                "  TOOLID varchar(50) default NULL," +
                "  AMOUNT int(11) default NULL," +
                "  PERSION varchar(50) default NULL," +
                "  UPDATETIME timestamp NOT NULL default CURRENT_TIMESTAMP" +
                ")");

        //��ݰ汾ͬ��
        db.execSQL("CREATE TABLE TR_DATA_SYNCHRONIZATION ("+
                " SYNCHRONIZATIONID varchar(36) NOT NULL PRIMARY KEY,"+
                " TABLENAME varchar(50),"+
                " KEYNAME varchar(50),"+
                " KEYNAMEVALUE varchar(50), "+
                " VERSIONNUMBER int(36),"+
                " RECORDTIME timestamp,"+
                " VERSIONPERSONID varchar(36) ,"+
                " OPERTYPE char(1) ,"+
                " DATATYPE char(1) , "+
                " FILEURL varchar(200), "+
                " FILENAME varchar(50))");

        //�����Ŀ����ɫ�ף���ݱ�
        db.execSQL("CREATE TABLE TR_SPECIAL_OLI_DATA ("+
                " ID varchar(36) NOT NULL PRIMARY KEY,"+
                " DETECTIONPROJECTID varchar(32),"+
                " DETECTIONMETHOD varchar(50),"+
                " DETECTIONSTANDARD varchar(500), "+
                " DETECTIONEXAMPLE varchar(100),"+
                " STATUS varchar(1),"+
                " REQUIRED varchar(1),"+
                " DATAFORMAT varchar(20),"+
                " DATAUNIT varchar(20),"+
                " DETECTIONDES varchar(50),"+
                " RESULT1 varchar(50),"+
                " RESULT2 varchar(50),"+
                " RESULT3 varchar(50),"+
                " RESULTDESCRIBE varchar(200),"+
                " MOBILDISPLAY varchar(1),"+
                " REMARKS varchar(200),"+
                " ORDERBY int(11)," +
                " UPDATETIME timestamp,"+
                " UPDATEPERSON varchar(50))");

        //������¼��
        db.execSQL("CREATE TABLE TR_SPECIAL_WORK ("+
                " ID varchar(36) NOT NULL PRIMARY KEY,"+
                " DETECTIONPROJECTID varchar(32),"+
                " DETECTIONDES varchar(50),"+
                " DETECTIONDATE date, "+
                " DETECTIONTIME varchar(50),"+
                " DETECTIONCONDITION varchar(1),"+
                " TEMP float,"+
                " RADIATION float,"+
                " LOADS float,"+
                " UPDATEPERSON varchar(50),"+
                " UPDATETIME timestamp,"+
                " DETECTIONDEPERSON varchar(100)," +
                " REMARKS varchar(200))");

        //��ѹ�������������¼
        db.execSQL("CREATE TABLE TR_NOISE_RECORD ("+
                " ID varchar(36) NOT NULL PRIMARY KEY,"+
                " DETECTIONOBJID varchar(36),"+
                " DCFAN int(11),"+
                " OILPUMP int(11), "+
                " NOISEAVG float)");

        //���������ֵ
        db.execSQL("CREATE TABLE TR_NOISE_RECORD_VALUE ("+
                " ID varchar(36) NOT NULL PRIMARY KEY,"+
                " GROUPID varchar(36),"+
                " SIGHT int(11),"+
                " HEIGHT13 float, "+
                " HEIGHT23 float)");
        //��ѹ�������������¼����
        db.execSQL("CREATE TABLE TR_NOISE_RECORD_GROUP ("+
                " ID varchar(36) NOT NULL PRIMARY KEY,"+
                " NOISEID varchar(36),"+
                " GROUPNAME varchar(36),"+
                " ORDERBY int(11))");

        //������(EQUIPMENTID:�豸ID��USEQUANTITY��ʹ������CONNECTTYPE���������ͣ�
        // BUSINESSTYPE��ҵ�����ͣ�RECORDTIME��ʹ��ʱ��)
        db.execSQL("CREATE TABLE SYMP_FLOW_INFO (" +
                "FLOWINFOID varchar(36) NOT NULL PRIMARY KEY," +//������ID
                "USERID  varchar(36)," +//�û�ID
                "FLOWTYPE  varchar(10)," +//�������ͣ��ı���ͼƬ
                "USETYPE  varchar(10)," +//ʹ�����ͣ��ϴ�������
                "CONNECTTYPE  varchar(10)," +//�������ͣ�wifi���ƶ�����ͨ
                "WEBTYPE  varchar(10)," +//�������ͣ�2G��3G��4G
                "RECORDTIME  DATE ," +//��ǰʱ��
                "USEQUANTITY  FLOAT ," +//ʹ����
                "EQUIPMENTID  varchar(35)," +//�豸ID����ǰ����ID
                "BUSINESSTYPE  varchar(10)," +//ҵ������
                "REMARK  varchar(200)" +//��ע
                ")");

        //��ͨ��������
        db.execSQL(" CREATE TABLE SYMP_COMMUNICATION_THEME(" +
                "ID varchar(50) NOT NULL PRIMARY KEY," +
                "BELONGID varchar(50)," +
                "POSITION varchar(100)," +
                "THEMETITLE varchar(50)," +
                "THEMECONTENT text," +
                "CREATEPERSONID varchar(50)," +
                "CREATETIME timestamp," +
                "THEMESTATE varchar(1)," +
                "PARTICIPATOR varchar(100) )");
        //��ͨ�����ظ�
        db.execSQL(" CREATE TABLE SYMP_COMMUNICATION_REPLY(" +
                "ID varchar(50) NOT NULL PRIMARY KEY," +
                "THEMEID varchar(50)," +
                "REPLYPERSONID varchar(50)," +
                "REPLYCONTENT text," +
                "REPLYTIME timestamp," +
                "REPLYID varchar(50)," +
                "REPLYOBJECTID varchar(50)," +
                "STATE varchar(1) )");
        //��ͨ��������
        db.execSQL(" CREATE TABLE SYMP_COMMUNICATION_PROMPT(" +
                "ID varchar(50) NOT NULL PRIMARY KEY," +
                "THEMEID varchar(50)," +
                "REPLYID varchar(50)," +
                "PROMPTCONTENT varchar(100)," +
                "PROMPTPERSONID varchar(50)," +
                "CREATETIME timestamp," +
                "STATE varchar(1)," +
                "PROMPTTYPE varchar(1) )");
        //��ͨ����������
        db.execSQL(" CREATE TABLE SYMP_COMMUNICATION_ACCESSORY(" +
                "ID varchar(50) NOT NULL PRIMARY KEY," +
                "THEMEID varchar(50)," +
                "REPLYID varchar(50)," +
                "FILEURL varchar(200)," +
                "FILENAME varchar(50)," +
                "REMARKS varchar(100)," +
                "RECORDTIME timestamp," +
                "ISUPLOAD varchar(2) default 0," +
                "ACCESSORYTYPE varchar(10))");

        //�?��
        db.execSQL(" CREATE TABLE TR_REPORT(" +
                "ID varchar(50) NOT NULL PRIMARY KEY," +
                "PROJECTID varchar(50)," +
                "STATIONID varchar(50)," +
                "STATIONNAME varchar(64)," +
                "REPORTNAME varchar(50)," +
                "CREATEPERSON varchar(50)," +
                "CREATETIME timestamp," +
                "STATUS varchar(50)," +
                "AUDITINGPERSON varchar(50)," +
                "AUDITINGSUGGESTION varchar(50)," +
                "AUDITINGTIME timestamp," +
                "WORDURL varchar(500))");
        //常用工具
        db.execSQL(" CREATE TABLE TR_COMMONTOOLS(" +
                "ID varchar(50) NOT NULL PRIMARY KEY," +
                "FILENAME varchar(50)," +
                "FILEURL varchar(100)," +
                "UPLOADPERSONID varchar(36)," +
                "TYPE varchar(2)," +
                "FILESIZE varchar(50)," +
                "UPLOADTIME timestamp," +
                "REMARKS varchar(200))");

        //Sd卡参数维护表
        db.execSQL(" CREATE TABLE tr_parameter(" +
                "ID varchar(36) NOT NULL," +
                "PARAMETERTYPE varchar(36) DEFAULT NULL," +
                "COMPANY varchar(100) DEFAULT NULL," +
                "MODEL varchar(36) DEFAULT NULL," +
                "SDFILEURL varchar(100) DEFAULT NULL," +
                "FILEPREFIX varchar(36) DEFAULT NULL," +
                "FILESUFFIX varchar(36) DEFAULT NULL," +
                "UPDATEPERSON varchar(36) DEFAULT NULL," +
                "UPDATETIME timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP)");

        //反馈主题表
        db.execSQL(" CREATE TABLE TR_FEEDBACK_THEME(" +
                "ID varchar(50) NOT NULL PRIMARY KEY," +//主键id
                "THEMETITLE varchar(50)," +//反馈主题
                "THEMECONTENT text," +//反馈内容
                "CREATEPERSONID varchar(50)," +//创建人
                "CREATETIME timestamp," +//创建时间
                "THEMESTATE varchar(1) )") ;//反馈状态


        //反馈回复
        db.execSQL(" CREATE TABLE TR_FEEDBACK_REPLY(" +
                "ID varchar(50) NOT NULL PRIMARY KEY," +//主键id
                "THEMEID varchar(50)," +//反馈主题id
                "REPLYPERSONID varchar(50)," +//反馈人id
                "REPLYCONTENT text," +//反馈内容
                "REPLYTIME timestamp," +//反馈时间
                "STATE varchar(1) )");//反馈状态

        //反馈附件表
        db.execSQL(" CREATE TABLE TR_FEEDBACK_ACCESSORY(" +
                "ID varchar(50) NOT NULL PRIMARY KEY," +//主键id
                "THEMEID varchar(50)," +//反馈主题id
                "REPLYID varchar(50)," +//回复id
                "FILEURL varchar(200)," +//文件路径
                "FILENAME varchar(50)," +//文件名称
                "RECORDTIME timestamp," +//回复时间
                "ISUPLOAD varchar(2) default 0," +//是否上传
                "ACCESSORYTYPE varchar(10))");//附件类型

        db.execSQL(" CREATE TABLE TR_EXCEPTION_INFO(" +
                "ID varchar(50) NOT NULL PRIMARY KEY," +//主键id
                "EXCEPTIONCLASS varchar(200) ," +//异常所在的类
                "EXCEPTIONMESSAGE text ," +//异常信息
                "USERID varchar(36) ,"+ //当前操作用户
                "EXCEPTIONFROM varchar(36) ,"+ //异常来源
                    "UPDATETIME timestamp)");//出现异常的时间

        //异常更新信息
        db.execSQL("CREATE TABLE TR_VERSIONUPDATE(" +
                "ID varchar(36) NOT NULL PRIMARY KEY," +//id
                "ADDINFO varchar(100) ," +//新增内容
                "UPDATEINFO varchar(100) ," +//修改及优化内容
                "DELETEINFO varchar(100) ,"+ //删除内容
                "VERSIONCODE int ,"+ //版本号
                "VERSIONNAME varchar(100) ,"+ //版本名称
                "LOADPERSON varchar(36) ,"+ //上传人
                "UPDATETIME timestamp)");//更新时间
    }


    /**
     * �����ݴ��ڸ��£���ִ�и÷���
     *
     * @param
     * @return void
     * @designer
     * @author zhaogr
     * @since 2014-11-6
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//            db.execSQL("drop TABLE tr_equipment");
        Log.i("==================","==oldVersion="+oldVersion);
        if(oldVersion == 4) {
            try {
                db.execSQL(" CREATE TABLE TR_COMMONTOOLS(" +
                        "ID varchar(50) NOT NULL PRIMARY KEY," +
                        "FILENAME varchar(50)," +
                        "FILEURL varchar(100)," +
                        "UPLOADPERSONID varchar(36)," +
                        "TYPE varchar(2)," +
                        "UPLOADTIME timestamp," +
                        "REMARKS varchar(200))");

                //Sd卡参数维护表
                db.execSQL(" CREATE TABLE tr_parameter(" +
                        "ID varchar(36) NOT NULL," +
                        "PARAMETERTYPE varchar(36) DEFAULT NULL," +
                        "COMPANY varchar(100) DEFAULT NULL," +
                        "MODEL varchar(36) DEFAULT NULL," +
                        "SDFILEURL varchar(100) DEFAULT NULL," +
                        "FILEPREFIX varchar(36) DEFAULT NULL," +
                        "FILESUFFIX varchar(36) DEFAULT NULL," +
                        "UPDATEPERSON varchar(36) DEFAULT NULL," +
                        "UPDATETIME timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP)");

                //反馈主题表
                db.execSQL(" CREATE TABLE TR_FEEDBACK_THEME(" +
                        "ID varchar(50) NOT NULL PRIMARY KEY," +//主键id
                        "THEMETITLE varchar(50)," +//反馈主题
                        "THEMECONTENT text," +//反馈内容
                        "CREATEPERSONID varchar(50)," +//创建人
                        "CREATETIME timestamp," +//创建时间
                        "THEMESTATE varchar(1) )") ;//反馈状态


                //反馈回复
                db.execSQL(" CREATE TABLE TR_FEEDBACK_REPLY(" +
                        "ID varchar(50) NOT NULL PRIMARY KEY," +//主键id
                        "THEMEID varchar(50)," +//反馈主题id
                        "REPLYPERSONID varchar(50)," +//反馈人id
                        "REPLYCONTENT text," +//反馈内容
                        "REPLYTIME timestamp," +//反馈时间
                        "STATE varchar(1) )");//反馈状态

                //反馈附件表
                db.execSQL(" CREATE TABLE TR_FEEDBACK_ACCESSORY(" +
                        "ID varchar(50) NOT NULL PRIMARY KEY," +//主键id
                        "THEMEID varchar(50)," +//反馈主题id
                        "REPLYID varchar(50)," +//回复id
                        "FILEURL varchar(200)," +//文件路径
                        "FILENAME varchar(50)," +//文件名称
                        "RECORDTIME timestamp," +//回复时间
                        "ISUPLOAD varchar(2) default 0," +//是否上传
                        "ACCESSORYTYPE varchar(10))");//附件类型

                db.execSQL("ALTER TABLE TR_COMMONTOOLS ADD FILESIZE varchar (50)");

            } catch (Exception e) {
                e.printStackTrace();
                Constants.recordExceptionInfo(e, context, context.toString()+"/SQLHelper");
            }
        }else if(oldVersion == 5){
            try {
                //反馈主题表
                db.execSQL(" CREATE TABLE TR_FEEDBACK_THEME(" +
                        "ID varchar(50) NOT NULL PRIMARY KEY," +//主键id
                        "THEMETITLE varchar(50)," +//反馈主题
                        "THEMECONTENT text," +//反馈内容
                        "CREATEPERSONID varchar(50)," +//创建人
                        "CREATETIME timestamp," +//创建时间
                        "THEMESTATE varchar(1) )") ;//反馈状态


                //反馈回复
                db.execSQL(" CREATE TABLE TR_FEEDBACK_REPLY(" +
                        "ID varchar(50) NOT NULL PRIMARY KEY," +//主键id
                        "THEMEID varchar(50)," +//反馈主题id
                        "REPLYPERSONID varchar(50)," +//反馈人id
                        "REPLYCONTENT text," +//反馈内容
                        "REPLYTIME timestamp," +//反馈时间
                        "STATE varchar(1) )");//反馈状态

                //反馈附件表
                db.execSQL(" CREATE TABLE TR_FEEDBACK_ACCESSORY(" +
                        "ID varchar(50) NOT NULL PRIMARY KEY," +//主键id
                        "THEMEID varchar(50)," +//反馈主题id
                        "REPLYID varchar(50)," +//回复id
                        "FILEURL varchar(200)," +//文件路径
                        "FILENAME varchar(50)," +//文件名称
                        "RECORDTIME timestamp," +//回复时间
                        "ISUPLOAD varchar(2) default 0," +//是否上传
                        "ACCESSORYTYPE varchar(10))");//附件类型

                db.execSQL("ALTER TABLE TR_COMMONTOOLS ADD FILESIZE varchar (50)");

            } catch (Exception e) {
                e.printStackTrace();
                Constants.recordExceptionInfo(e, context, context.toString()+"/SQLHelper");
            }
        }else if(oldVersion == 6){
            try {
                db.execSQL("ALTER TABLE TR_COMMONTOOLS ADD FILESIZE varchar (50)");
            } catch (Exception e) {
                e.printStackTrace();
                Constants.recordExceptionInfo(e, context, context.toString()+"/SQLHelper");
            }
        }
    }
}