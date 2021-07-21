package com.github.dreamroute.tx.manager.sample.service.impl;

import com.github.dreamroute.tx.manager.sample.domain.Addr;
import com.github.dreamroute.tx.manager.sample.mapper.AddrMapper;
import com.github.dreamroute.tx.manager.sample.service.AddrService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class AddrServiceImpl implements AddrService {

    private final AddrMapper addrMapper;

    @Override
//    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void insert(Addr addr) {
        addrMapper.insert(addr);
    }
}
