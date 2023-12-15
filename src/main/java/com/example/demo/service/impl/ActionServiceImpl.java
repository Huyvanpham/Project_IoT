package com.example.demo.service.impl;

import com.example.demo.dto.ActionDto;
import com.example.demo.dto.SearchDto;
import com.example.demo.model.Action;
import com.example.demo.repository.ActionRepository;
import com.example.demo.service.ActionService;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class ActionServiceImpl implements ActionService {
    Logger logger = LoggerFactory.getLogger(ActionServiceImpl.class);
    private final ActionRepository actionRepository;
    private final EntityManager entityManager;
    @Autowired
    public ActionServiceImpl(ActionRepository actionRepository,
                             EntityManager entityManager)
    {
        this.actionRepository = actionRepository;
        this.entityManager = entityManager;
    }

    @Override
    public List<Action> getAllActions() {
        return this.actionRepository.getAllAction();
    }

    @Override
    public void getByCondition(SearchDto searchDto) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        StringBuilder selectSql = new StringBuilder( "Select new com.example.demo.dto.ActionDto(a) from Action a where (1 = 1)" );
        StringBuilder countSql = new StringBuilder("Select count(a.id) from Action a where (1=1)");


        if(!ObjectUtils.isEmpty( searchDto.getStartDate()))
        {
            selectSql.append(" and a.time >= ?1");
            countSql.append(" and a.time >= ?1");
        }

        if(!ObjectUtils.isEmpty( searchDto.getEndDate()))
        {
            selectSql.append(" and a.time <= ?2");
            countSql.append(" and a.time <= ?2");
        }

        if(!ObjectUtils.isEmpty(searchDto.getOrderDate()))
        {
            if(searchDto.getOrderDate().equals("desc"))
            {
                selectSql.append(" order by a.time desc");
            }
            else
            {
                selectSql.append(" order by a.time asc");
            }
        }

        Query countQuery = this.entityManager.createQuery(countSql.toString());
        Query selectQuery = this.entityManager.createQuery(selectSql.toString());

        if(!ObjectUtils.isEmpty(searchDto.getStartDate())){
            try {
                Timestamp batDau = new Timestamp(sdf.parse(searchDto.getStartDate()).getTime());
                countQuery.setParameter(1, batDau);
                selectQuery.setParameter(1, batDau);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if(!ObjectUtils.isEmpty(searchDto.getEndDate())){
            try {
                Timestamp ketThuc = new Timestamp(sdf.parse(searchDto.getEndDate()).getTime());
                countQuery.setParameter(2, ketThuc);
                selectQuery.setParameter(2, ketThuc);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        long totalItems = (long) countQuery.getSingleResult();
        if(searchDto.getPageIndex() == null || searchDto.getPageIndex() <= 0)
            searchDto.setPageIndex(1);
        if(searchDto.getPageSize() == null)
            searchDto.setPageSize(10);

        selectQuery.setFirstResult( (searchDto.getPageIndex() - 1) *  searchDto.getPageSize()).setMaxResults(searchDto.getPageSize());
        List<ActionDto> result = selectQuery.getResultList();
        searchDto.setResult(result);
        searchDto.setTotalPages( (int) Math.ceil(1.0 * totalItems / searchDto.getPageSize()));
        searchDto.setValid(true);
    }

    @Override
    public Action saveAction(JsonObject jsonObject) {
        Action newAction = new Action();
        newAction.setTime(new Timestamp(new Date().getTime()));
        JsonElement led1 = jsonObject.get("led1");
        if(!ObjectUtils.isEmpty(led1))
        {
            newAction.setThietBi("Đèn 1");
            String led1Message = led1.toString();
            if(led1Message.equals("\"on\""))
                newAction.setTrangThai("bật");
            else if(led1Message.equals("\"off\""))
                newAction.setTrangThai("tắt");
        }
        JsonElement led2 = jsonObject.get("led2");
        if(!ObjectUtils.isEmpty(led2))
        {
            newAction.setThietBi("Đèn 2");
            String led2Message = led2.toString();
            if (led2Message.equals("\"on\""))
                newAction.setTrangThai("bật");
            else if (led2Message.equals("\"off\""))
                newAction.setTrangThai("tắt");
        }
        JsonElement led3 = jsonObject.get("led3");
        if(!ObjectUtils.isEmpty(led3))
        {
            newAction.setThietBi("Đèn 3");
            String led3Message = led3.toString();
            if (led3Message.equals("\"on\""))
                newAction.setTrangThai("bật");
            else if (led3Message.equals("\"off\""))
                newAction.setTrangThai("tắt");
        }

        return this.actionRepository.save(newAction);
    }
}