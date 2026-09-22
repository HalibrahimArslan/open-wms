package com.hisarresearch.wms.web.rest;

import com.hisarresearch.wms.domain.CountingAddressException;
import com.hisarresearch.wms.service.CountingAddressExceptionQueryService;
import com.hisarresearch.wms.service.CountingAddressExceptionService;
import com.hisarresearch.wms.service.criteria.CountingAddressExceptionCriteria;
import com.hisarresearch.wms.service.dto.CountingAddressExceptionDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.hisarresearch.wms.framework.web.util.PaginationUtil;


import java.util.List;

@RestController
@RequestMapping("/api")
@Transactional
public class CountingAddressExceptionResource {
    private final Logger log = LoggerFactory.getLogger(CountingAddressExceptionResource.class);


    private final CountingAddressExceptionService countingAddressExceptionService;

    private final CountingAddressExceptionQueryService countingAddressExceptionQueryService;

    public CountingAddressExceptionResource(CountingAddressExceptionService countingAddressExceptionService, CountingAddressExceptionQueryService countingAddressExceptionQueryService) {
        this.countingAddressExceptionService = countingAddressExceptionService;
        this.countingAddressExceptionQueryService = countingAddressExceptionQueryService;
    }

    /**
     * {@code GET /counting-address-exceptions} : get all counting-address-definition list.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body all counting-address-exception-list.
     */
    @GetMapping("/counting-address-exceptions")
    public ResponseEntity<List<CountingAddressException>> getCountingAddressExceptionList(CountingAddressExceptionCriteria criteria,Pageable pageable){
        log.debug("REST request to get CountingAddressExceptions by criteria: {}", criteria);
        Page<CountingAddressException> page = countingAddressExceptionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET /counting-address-exceptions/count} : get count of counting-address-definition list.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with count of counting-address-exception-list.
     */
    @GetMapping("/counting-address-exceptions/count")
    public ResponseEntity<Long> getCountingAddressExceptionList(CountingAddressExceptionCriteria criteria){
        log.debug("REST request to get count of CountingAddressExceptions by criteria: {}", criteria);
        return ResponseEntity.ok().body(countingAddressExceptionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code POST /counting-address-exception} : get all counting-address-definition list.
     *
     * @return the {@link ResponseEntity} with status {@code 201 (CREATED)} and with created counting-address-exception.
     */
    @PostMapping("/counting-address-exception")
    public ResponseEntity<CountingAddressException> saveCountingAddressException(@RequestBody CountingAddressExceptionDto dto)  {
        log.debug("REST request to save CountingAddressException : {}", dto);
        CountingAddressException savedOne = countingAddressExceptionService.saveCountingAddressException(dto);
        return new ResponseEntity<>(savedOne,HttpStatus.CREATED);
    }

    /**
     * {@code POST /counting-address-exception-bulk} : save bulk counting address exception.
     *
     * @return the {@link ResponseEntity} with status {@code 201 (CREATED)} and with created list of counting-address-exception.
     */
    @PostMapping("/counting-address-exception-bulk")
    public ResponseEntity<List<CountingAddressException>> saveCountingAddressExceptionBulk(@RequestBody List<CountingAddressExceptionDto> countingAddressExceptionDtoList)  {
        log.debug("REST request to bulk save CountingAddressException : {}", countingAddressExceptionDtoList);
        List<CountingAddressException> savedList = countingAddressExceptionService.saveCountingAddressExceptionBulk(countingAddressExceptionDtoList);
        return new ResponseEntity<>(savedList,HttpStatus.CREATED);
    }

    @DeleteMapping("/counting-address-exception/{id}")
    public ResponseEntity<Void> updateCountingAddressException(@PathVariable Long id){
       log.debug("REST request to update CountingAddressException : {}", id);
        countingAddressExceptionService.deleteOne(id);
        return ResponseEntity.ok().build();
    }

}
