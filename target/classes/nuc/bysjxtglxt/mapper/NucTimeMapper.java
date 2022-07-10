package nuc.bysjxtglxt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import nuc.bysjxtglxt.domain.NucRecord;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface NucTimeMapper extends BaseMapper<NucRecord> {
    Date compareTime(@Param("lastTime") Date lastTime);
    IPage<NucRecord>findMyTime(Page<NucRecord> page,@Param("lastTime") Date date);
}
