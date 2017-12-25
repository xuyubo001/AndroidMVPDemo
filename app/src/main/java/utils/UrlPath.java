package utils;

/**
 * 作用:用来存储访问地址的URL
 */
public class UrlPath {
    /***********************
     * 基本地址 正牌服务器在getServerPaht中改为/arkink-homework-services/
     * 高飞测试地址/lamppa-homework-services/
     **/

    public static String BASEIP = "192.168.0.65";
    public static String PORT = "8282";// 端口号

    private static String getBaseIP() {
        return BASEIP;
    }

    private static String getPort() {
        return PORT;
    }

    public static String getBASEURL() {
        return "http://" + getBaseIP() + ":" + getPort() + "/";
    }

    public static String getSERVERPATH() {
//        return getBaseIP() + ":" + getPort() + "/lamppa-homework-services/";
        return "http://" + getBaseIP() + ":" + getPort() + "/lamppa-homework-app/";
    }

    /**
     * 功能地址
     */

    // 传给服务端个推CID
    public static String UPDATE_CLIENT_ID() {
        return getSERVERPATH() + "login/updateClientId";
    }

    // 登录功能
    public static String PATHLOGIN() {
        return getSERVERPATH() + "login/user";
    }

 // 获取帮助说明
    public static String PATH_HELP() {
    	return getSERVERPATH() + "help/getHelpUrl";
    }
    // 根据教师信息获取所带学科信息
    public static String PATHSUBJECT() {
        return getSERVERPATH() + "baseData/subject";
    }

    // 根据老师带的科目，获取老师所带班级
    public static String PATHEDUSUBJECTCLASS() {
        return getSERVERPATH() + "baseData/eduSubjectClass";
    }

    // 根据老师所选学科，获取其所带学科的教材版本
    public static String PATHBOOKVERSION() {
        return getSERVERPATH() + "baseData/bookVersion";
    }

    // 根据老师信息获取所带年级的基本信息
    public static String PATHGETGRADE() {
        return getSERVERPATH() + "metadata/getGrade";
    }

    // 根据教师所选学科，年级，教材版本获取分册信息
    //    public static String PATHGETFASCICULE() {
    //        return getSERVERPATH() + "baseData/getFascicule";
    //    }版本一中查询的分册内容下面试最新的
    public static String PATHGETFASCICULE() {
        return getSERVERPATH() + "baseData/getTextbook";
    }

    // 根据老师所选则分册的Key（也就是ID）获取章节的菜单树列表
    public static String PATHGETCHAPTERSECTIONTREE() {
        return getSERVERPATH() + "baseData/getChapterSectionTree";
    }

    // 获取试题类型信息接口
    public static String PATHGETQUESTIONTYPE() {
        return getSERVERPATH() + "baseData/getQuestionType";
    }

    // 根据班级key（id）获取班学生的基本信息
    public static String PATHGETSTUBYCLASSID() {
        return getSERVERPATH() + "baseData/getStuByClassId";
    }
    
    // 根据班级ID获取该班级所有学生
    public static String GETSTUTENTSBYCLASSID() {
        return getSERVERPATH() + "baseData/getStuByClassId";
    }
    
    
    // 根据班级ID获取该班级所有学生
    public static String SHOWGROUP() {
        return getSERVERPATH() + "baseData/showGroups";
    }
    
    //获取班级所有小组
    public static String GETGROUPLIST() {
        return getSERVERPATH() + "baseData/getGroupTitleList";
    }
    
    //获取小组中人员及老师的信息
    public static String SHOWGROUPS() {
        return getSERVERPATH() + "baseData/showGroups";
    }

    /*************************
     * 题库地址
     *********************************/
    // 布置作业:根据老师所选择信息获取试题信息
    public static String PATHSEARCHQUESTION() {
        return getSERVERPATH() + "template/searchQuestion";
    }

    // 保存试卷信息
    public static String PATHSAVETEMPLATE() {
        return getSERVERPATH() + "template/saveTemplate";
    }

    // 删除试卷信息
    public static String PATHDELETETEMPLATE() {
        return getSERVERPATH() + "template/deleteTemplate";
    }

    /**
     * 查询分册和出版时间
     * @return
     */
    public static String GET_TEXT_BOOK() {
    	return getSERVERPATH() + "metadata/getTextbook";
    }
    /*************************
     * 下发作业地址
     *********************************/
    // 根据老师所选择信息查询试卷信息接口
    public static String PATHSEARCH() {
        return getSERVERPATH() + "template/search";
    }

    // 下发题库作业
    public static String PATHSENDQUESTIONWORK() {
        return getSERVERPATH() + "taskRelease/sendQuestionWork";
    }
    

    // 分层下发作业接口
    public static String PUSHHOMEWORK() {
        return getSERVERPATH() + "paperRelease/libWorkForLevel";
    }
    

    // 下发传统作业接口
    public static String SENDTRADITIONWORK() {
        return getSERVERPATH() + "taskRelease/sendTraditionWork";
    }

    /**
     * 传统作业上传语音接口
     */
    public static String SUBMIT_RECORD() {
        return getSERVERPATH() + "audioFile/upload";
    }
    /**
     * 传统作业上传图片接口
     */
    public static String SUBMIT_IMAGE() {
    	return getSERVERPATH() + "traImage/upload";
    }
    
    /**************************************************************
     * 自我出题接口地址
     **************************************************************/
    /**
     * 查询自己出题试卷接口
     */
    public static String GET_SELFASSIGN_HOMEWORK() {
        return getSERVERPATH() + "atWillWork/getAtWill";
    }

    /**
     * 下发自我出题作业接口(随意做)
     */
    public static String SEND_SELFASSIGN_HOMEWORK() {
        return getSERVERPATH() + "atWillWork/sendWork";
    }

    /**
     * 分层下发随意做
     */
    public static String SENDHOMEWORKATWILL() {
        return getSERVERPATH() + "atWillWork/sendWorkForLevel";
    }

    
    /**
     * 上传随意做压缩包借口
     */
    public static String SEND_FREEZIP_HOMEWORK() {
        return getSERVERPATH() + "atWillWork/uploadQuestionPic";
    }

    /**
     * 保存随意做和出题接口
     */
    public static String SAVE_FREE_HOMEWORK() {
        return getSERVERPATH() + "atWillWork/saveWork";

    }

    /**************************************************************
     * 批改作业接口地址
     **************************************************************/
    /**
     * 获取待批改和需复批作业接口
     */
    public static String LOOKUP_HOMEWORK() {
        return getSERVERPATH() + "check/lookup";
    }

    /**
     * 获取学生提交作业信息接口
     */
    public static String GET_STUDENT_HOMEWORK_INFO() {
        return getSERVERPATH() + "check/downloadCommit";
    }
    /**
     * 获取学生信息和作业信息的接口
     */
    public static String GET_STUDENT_RESULT() {
    	return getSERVERPATH() + "releaseResult/studentResult";
    }
    /**
     * 获取学生答案详情
     */
    public static String GET_VIEW_STUDENT() {
    	return getSERVERPATH() + "releaseResult/viewStudent";
    }

    /**
     * 获取待批改和需复批作业接口
     */
    public static String GET_CORRECT_HOMEWORK() {
        return getSERVERPATH() + "check/lookup";
    }

    /**
     * 撤回整个作业接口
     */
    public static String REVOKE_HOMEWORK() {
        return getSERVERPATH() + "check/revokeTaskRelease";
    }

    /**
     * 上传批改图片压缩包接口
     */
    public static String UPLOAD_IMAGE_ZIP() {
        return getSERVERPATH() + "check/uploadMarkPic";
    }

    /**
     * 作业打回接口
     */
    public static String REWRITE() {
        return getSERVERPATH() + "check/sendRedo";
    }

    /**
     * 完成批改接口
     */
    public static String COMPLETE_HOMEWORK() {
        return getSERVERPATH() + "check/markAndComplete";
    }

    /**
     * 评语框提交语音
     */
    
    public static String SUBMIT_COMMENT_BOX_RECORD(){
    	return getSERVERPATH() + "check/uploadSpeak";
    }
    
    /**
     * 根据学科获取作业本类型接口
     */
    public static String GETWORKBOOKTYPE() {
        return getSERVERPATH() + "baseData/getWorkBookType";
    }

    /**
     * 上传批改图片压缩包接口
     */
    public static String UPLOAD_MARK_PIC() {
        return getSERVERPATH() + "check/uploadMarkPic";
    }

    /**
     * 修改作业状态
     */
    public static String CHANGE_STATE() {
        return getSERVERPATH() + "taskRelease/changeStatus";
    }

    /**
     * 得到试卷所有学生信息
     */
    public static String GET_TASK_STUDENT_INFO() {
        return getSERVERPATH() + "taskEvaluate/showStudent";
    }

    /**
     * 完成所有学生批改
     */
    public static String DONE_ALL_CORRECTED() {
        return getSERVERPATH() + "check/complateOver";
    }

    public static String GET_COMMITWORK() {
        return getSERVERPATH() + "check/getCommitWork";
    }

    // 检查更新借口
    public static String AUTOUPDATE() {
        return getSERVERPATH() + "baseData/getVersion";
    }


    /**************************************************************
     * 分析作业接口
     **************************************************************/
    /**
     * 1作业统计
     */
    public static String HOMEWORKSTATISTICS() {
        return getSERVERPATH() + "statistics/homeworkStatistics";
    }

    /**
     * 2查看作业人数统计(查看页面)
     */
    public static String HOMEWORKUNCOMMIT() {
        return getSERVERPATH() + "statistics/homeworkUnCommit";
    }

    /**
     * 3作业发布
     */
    public static String HOMEWORKRELEASE() {
        return getSERVERPATH() + "releaseResult/publish";
    }

    /**
     * 3作业提醒
     */
    public static String HOMEWOREMIND() {
        return getSERVERPATH() + "paperRelease/remind";
    }

    /**
     * 4作业的完成情况统计
     */
    public static String HOMEWORKCOMPLETION() {
        return getSERVERPATH() + "statistics/homeworkCompletion";
    }

    /**
     * 5作业结果统计
     */
    public static String HOMEWORKRESULT() {
        return getSERVERPATH() + "releaseResult/analyzeQuestion";
    }
    /**
     * 获取该作业的学生
     */
    public static String GETSTUDENTSTATUS() {
    	return getSERVERPATH() + "releaseResult/studentStatus";
    }
    /**
     * 典型对错数据
     */
    public static String TYPICALANSWER() {
    	return getSERVERPATH() + "releaseResult/typicalAnswer";
    }
//    public static String HOMEWORKRESULT() {
//    	return getSERVERPATH() + "statistics/homeworkResult";
//    }

    
    /**
     * 6作业挑战题统计
     */
    public static String HOMEWORKCHALLENGE() {
        return getSERVERPATH() + "statistics/homeworkChallenge";
    }

    /**
     * 7题目统计
     */
    public static String HOMEWORKQUESTION() {
        return getSERVERPATH() + "statistics/homeworkQuestion";
    }
    /**
     * 8获取学生分数
     */
    public static String STUDENTSCORE() {
    	return getSERVERPATH() + "taskEvaluate/viewStatistics";
    }

    /**
     * 获取前十名学生
     */
    public static String PODIUMLIST(){
    	return getSERVERPATH() + "releaseResult/submitSpeed";
    }
    /**************************************************************
     * 作业APP升级接口
     **************************************************************/
    public static String DOWNLOADHOMEWORKAPK() {
        return getSERVERPATH() + "apk/downLoadHomeworkApk";
    }
}
