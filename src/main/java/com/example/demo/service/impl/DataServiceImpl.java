package com.example.demo.service.impl;

import com.example.demo.model.Data;
import com.example.demo.dto.DataDto;
import com.example.demo.dto.SearchDataDto;
import com.example.demo.model.LedStatus;
import com.example.demo.repository.ActionRepository;
import com.example.demo.repository.DataRepository;
import com.example.demo.service.DataService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DataServiceImpl implements DataService {
    private final DataRepository dataRepository;
    private final ActionRepository actionRepository;
    private final EntityManager entityManager;
    private final Logger logger = LoggerFactory.getLogger(DataServiceImpl.class);

    @Override
    public List<DataDto> get7LastestResult(LedStatus ledStatus)
    {
        //Đếm số lượng bản ghi
        long numberOfRecord = this.dataRepository.count();

        Query query = entityManager.createQuery("Select new com.example.demo.dto.DataDto(d) from Data d ").setFirstResult(numberOfRecord > 7 ? (int)numberOfRecord - 7 : 0).setMaxResults(7);
        List<DataDto> result = query.getResultList();

        int[] countDen = new int[4];
        countDen[0] = this.actionRepository.countDen1Bat();
        countDen[1] = this.actionRepository.countDen1Tat();
        countDen[2] = this.actionRepository.countDen2Bat();
        countDen[3] = this.actionRepository.countDen2Tat();

        if (!ObjectUtils.isEmpty(result)) {
            result.get(result.size() - 1).setCountThietBiBatTat(countDen);
            result.get(result.size() - 1).setLedStatus(ledStatus);
        }

        return result;
    }

    @Override
    public List<DataDto> getAllResult()
    {
        Query query = entityManager.createQuery("Select new com.example.demo.dto.DataDto(d) from Data d order by d.id desc ");
        return query.getResultList();
    }

    @Override
    public void getByCondition(SearchDataDto searchDataDto)
    {
        String[] messaages = new String[10];
        boolean isValid = true;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        StringBuilder selectSql = new StringBuilder( "Select new com.example.demo.dto.DataDto(d) from Data d where (1 = 1)" );
        StringBuilder countSql = new StringBuilder("Select count(d.id) from Data d where (1=1)");

        System.out.println(searchDataDto.getTemperatureValue() + " " + searchDataDto.getHumidityValue() + " " + searchDataDto.getLightValue());

        if(!ObjectUtils.isEmpty(searchDataDto.getStartDate()))
        {
            selectSql.append(" and d.time >= ?1");
            countSql.append(" and d.time >= ?1");
        }

        if(!ObjectUtils.isEmpty(searchDataDto.getEndDate()))
        {
            selectSql.append(" and d.time <= ?2");
            countSql.append(" and d.time <= ?2");
        }

        if(!ObjectUtils.isEmpty(searchDataDto.getTemperatureValue()))
        {
            Double temo = Double.parseDouble(searchDataDto.getTemperatureValue());
            String sql = " and d.temperature >=" + (temo - 0.01) + " and d.temperature <= " + (temo + 0.01);
            selectSql.append(sql);
            countSql.append(sql);
        }

        if(!ObjectUtils.isEmpty(searchDataDto.getHumidityValue()))
        {
            Double temp = Double.parseDouble(searchDataDto.getHumidityValue());
            String sql = " and d.humidity >= " + (temp - 0.01) + " and d.humidity <= " + (temp + 0.01);
            selectSql.append(sql);
            countSql.append(sql);
        }

        if(!ObjectUtils.isEmpty(searchDataDto.getLightValue()))
        {
            Double temp = Double.parseDouble(searchDataDto.getLightValue());
            String sql = " and d.light >= " + (temp - 0.01) + " and d.light <= " + (temp + 0.01);
            selectSql.append(sql);
            countSql.append(sql);
        }

        if(!ObjectUtils.isEmpty(searchDataDto.getDoBuiValue()))
        {
            Double temp = Double.parseDouble(searchDataDto.getDoBuiValue());
            String sql = " and d.doBui >= " + (temp - 0.1) + " and d.doBui <= " + (temp + 0.1);
            selectSql.append(sql);
            countSql.append(sql);
        }

        this.addOrderClause(searchDataDto, selectSql);

        Query countQuery = this.entityManager.createQuery(countSql.toString());
        Query selectQuery = this.entityManager.createQuery(selectSql.toString());

        if(!ObjectUtils.isEmpty(searchDataDto.getStartDate())){
            try {
                Timestamp batDau = new Timestamp(sdf.parse(searchDataDto.getStartDate()).getTime());
                countQuery.setParameter(1, batDau);
                selectQuery.setParameter(1, batDau);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if(!ObjectUtils.isEmpty(searchDataDto.getEndDate())){
            try {
                Timestamp ketThuc = new Timestamp(sdf.parse(searchDataDto.getEndDate()).getTime());
                countQuery.setParameter(2, ketThuc);
                selectQuery.setParameter(2, ketThuc);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        long totalItems = (long) countQuery.getSingleResult();
        if(searchDataDto.getPageIndex() == null || searchDataDto.getPageIndex() <= 0)
            searchDataDto.setPageIndex(1);
        if(searchDataDto.getPageSize() == null)
            searchDataDto.setPageSize(10);

        selectQuery.setFirstResult( (searchDataDto.getPageIndex() - 1) *  searchDataDto.getPageSize()).setMaxResults(searchDataDto.getPageSize());

        List<DataDto> result = selectQuery.getResultList();
        searchDataDto.setResult(result);
        searchDataDto.setTotalPages( (int) Math.ceil(1.0 * totalItems / searchDataDto.getPageSize()));

        searchDataDto.setValid(isValid);
    }

    private void addOrderClause(SearchDataDto searchDataDto, StringBuilder selectSql)
    {
        boolean isFirstTime = true;

        if(!ObjectUtils.isEmpty(searchDataDto.getOrderDate()))
        {
            if(searchDataDto.getOrderDate().equals("desc"))
            {
                isFirstTime = false;
                selectSql.append(" order by d.time desc");
            }
            else
            {
                isFirstTime = false;
                selectSql.append(" order by d.time asc");
            }
        }
    }

    @Override
    public void saveData(Data data)
    {
        this.dataRepository.save(data);
    }
}
