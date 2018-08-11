package com.goldsudo.controller;

import com.goldsudo.bean.Seckill;
import com.goldsudo.dto.Exposer;
import com.goldsudo.dto.SeckillExcution;
import com.goldsudo.dto.SeckillResult;
import com.goldsudo.enums.SeckillStateEnum;
import com.goldsudo.exception.RepeatKillException;
import com.goldsudo.exception.SeckillCloseException;
import com.goldsudo.service.ISeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @ClassName: SeckillController
 * @Description: TODO
 * @Author: goldsudo
 * @Date: 2018/8/7
 */
@Controller
@RequestMapping("/seckill")
public class SeckillController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ISeckillService seckillSerivce;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public String list(Model model) {
        List<Seckill> list = seckillSerivce.getSeckills(0,10);
        for(Seckill seckill:list){
            logger.info("seckill="+seckill.toString());
        }
        model.addAttribute("list",list);
        //  /WEB-INF/jsp/list.jsp
        return "list";
    }

    @RequestMapping(value = "/{seckillId}/detail",method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long seckillId, Model model) {
        if (seckillId == null) {
            return "redirect:/seckill/list";
        }
        Seckill seckill = seckillSerivce.getSeckillById(seckillId);
        logger.info("seckill=" + seckill.toString());
        if (seckill == null) {
            return "forward:/seckill/list";
        }
        model.addAttribute("seckill", seckill);
        return "detail";
    }

    @RequestMapping(value = "/{seckillId}/exposer",
            method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<Exposer> exposer(@PathVariable Long seckillId) {
        SeckillResult<Exposer> result;
        try {
            Exposer exposer = seckillSerivce.exportSeckillUrl(seckillId);

            result = new SeckillResult<Exposer>(true, exposer);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            result = new SeckillResult<Exposer>(false, e.getMessage());
        }
        return result;
    }


    @RequestMapping(value = "/{seckillId}/{md5}/excution",
            method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<SeckillExcution> excute(@PathVariable("seckillId") Long seckillId,
                                                 @PathVariable("md5") String md5,
                                                 @CookieValue(value = "killPhone",required = false) Long phone) {
        if (phone == null) {
            return new SeckillResult<SeckillExcution>(false, "未注册！");
        }
        SeckillResult<SeckillExcution> result;
        try {
            SeckillExcution seckillExcution = seckillSerivce.excuteSeckill(seckillId, phone, md5);
            return new SeckillResult<SeckillExcution>(true, seckillExcution);
        } catch (RepeatKillException e) {
            //重复秒杀
            logger.error(e.getMessage(),e);
            SeckillExcution seckillExcution = new SeckillExcution(seckillId, SeckillStateEnum.REPEAT_KILL);
            return new SeckillResult<SeckillExcution>(true, seckillExcution);
        } catch (SeckillCloseException e) {
            //秒杀关闭
            logger.error(e.getMessage(),e);
            SeckillExcution seckillExcution = new SeckillExcution(seckillId, SeckillStateEnum.END);
            return new SeckillResult<SeckillExcution>(true, seckillExcution);
        } catch (Exception e) {
            //其他所有错误
            logger.error(e.getMessage(),e);
            SeckillExcution seckillExcution = new SeckillExcution(seckillId, SeckillStateEnum.INNER_ERROR);
            return new SeckillResult<SeckillExcution>(true, seckillExcution);
        }
    }

    @RequestMapping(value = "/time/now",method=RequestMethod.GET,produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<Long> time() {
        Date now = new Date();
        return new SeckillResult(true, now.getTime());
    }
}
