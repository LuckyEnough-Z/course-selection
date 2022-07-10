package nuc.bysjxtglxt.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import nuc.bysjxtglxt.controller.TimeController;
import nuc.bysjxtglxt.domain.BysjResponse;
import nuc.bysjxtglxt.domain.NucRecord;
import nuc.bysjxtglxt.domain.NucTopic;
import nuc.bysjxtglxt.domain.NucUser;
import nuc.bysjxtglxt.mapper.NucRecordMapper;
import nuc.bysjxtglxt.mapper.NucTopicMapper;
import nuc.bysjxtglxt.service.StudentService;
import nuc.bysjxtglxt.service.TeacherService;
import nuc.bysjxtglxt.util.QueryRequest;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @Author: jurui
 * @Email: acescen@foxmail.com
 * @ProjectName: bysjxtglxt
 * @Package: nuc.bysjxtglxt.service.impl
 * @Description:
 * @Date: Created in 9:45 2019-12-17
 */

@Service
public class StudentServiceImpl extends ServiceImpl<NucRecordMapper, NucRecord> implements StudentService {
    @Autowired
    private NucTopicMapper topicMapper;


    @Override
    public IPage<NucTopic> findMyTopicByAny(String any, QueryRequest request) {
        //获取当前登录用户信息
        NucUser user = (NucUser) SecurityUtils.getSubject().getPrincipal();


        Page<NucTopic> page = new Page<>(request.getPageNum(), request.getPageSize());

        return baseMapper.findMySlelect(page, any, user.getUserId());
    }


    @Override
    @Transactional
    public BysjResponse select(String topicId) {
        BysjResponse bysjResponse = new BysjResponse();
        //获取当前登录用户信息
        NucUser user = (NucUser) SecurityUtils.getSubject().getPrincipal();

        //首先判断该用户是否已选择题目
        Integer count = baseMapper.findByUserId(user.getUserId());
        TimeController Timer = new TimeController();


        if (count != null && count > 0) {
            bysjResponse.fail().message("你已经选了一个题目了😅😅");
//            bysjResponse.fail().message("1");
        } else{
            System.out.println("害没选呢，退什么");
            //判断题目是否存在
            NucTopic nucTopic = topicMapper.findmyTopicById(topicId);

            if (nucTopic != null) {
                //判断题目是否被选择
                if ("未被选择".equals(nucTopic.getStatus())) {
                    //将题目状态改为选择
                    nucTopic.setStatus("已被选");
                    topicMapper.updateById(nucTopic);
                    //记录到选题表
                    NucRecord nucRecord = new NucRecord(nucTopic.getTopicId(), user.getUserId(), new Date());
                    int insert = baseMapper.insert(nucRecord);

                    if (insert > 0) {
                        bysjResponse.success().message("选题成功🥰，到选题页面查看详情");
                    } else {
                        bysjResponse.fail().message("未知异常，选择失败！");

                    }
                } else {
                    bysjResponse.fail().message("该毕业设计题目已经被抢走了");
                }
            } else {
                bysjResponse.fail().message("该毕业设计题目不存在！");
            }
        }


        return bysjResponse;
    }

    @Override
    @Transactional
    public boolean deleteSel() {

        //获取当前登录用户信息
        NucUser user = (NucUser) SecurityUtils.getSubject().getPrincipal();

        //将题目表中的题目状态更改为未被选择
        boolean upTop =topicMapper.updateTopicStatus(user.getUserId());

        //删除选题记录表中的数据
        boolean upRec =baseMapper.deleteSel(user.getUserId());

        if (upRec && upTop) {
            return true;
        } else {
            return false;
        }
    }


    public static void main(String[] args) {
        NucRecord nucRecord = new NucRecord();
        TimeController time = new TimeController();
        System.out.println(time.timer());
        System.out.println(nucRecord);
    }
}
