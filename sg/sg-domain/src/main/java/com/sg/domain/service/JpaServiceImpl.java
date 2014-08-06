/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.service;

import com.sg.domain.dto.CanvasDto;
import com.sg.domain.dto.CanvasRefDto;
import com.sg.domain.dto.CanvasUpdateDto;
import com.sg.domain.dto.ThreadDto;
import com.sg.domain.dto.ThreadRefDto;
import com.sg.domain.dto.ThreadUpdateDto;
import com.sg.domain.entities.jpa.Canvas;
import com.sg.domain.entities.jpa.CanvasesRepository;
import com.sg.domain.entities.jpa.ProductRepository;
import com.sg.domain.entities.jpa.ThreadsRepository;
import java.util.List;
import javax.annotation.Resource;
import org.dozer.Mapper;
import org.springframework.transaction.support.TransactionTemplate;
import com.sg.domain.entities.jpa.Thread;
import java.util.ArrayList;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

/**
 *
 * @author tarasev
 */
//@Transactional annotation prevent spring context to be initialized on GAE
@Service
public class JpaServiceImpl implements SgService {

    @Resource(name = "productRepository")
    ProductRepository productRepository;

    @Resource(name = "threadsRepository")
    ThreadsRepository threadsRepository;
    
    @Resource(name = "canvasesRepository")
    CanvasesRepository canvasesRepository;

    @Resource(name = "mapper")
    Mapper mapper;

    @Resource(name = "transactionTemplate")
    private TransactionTemplate transactionTemplate;
    
    public void delete(final ThreadRefDto dto) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    deleteThreadImpl(dto);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
    
    private void deleteThreadImpl(ThreadRefDto dto) {
        Thread thread = threadsRepository.findByCode(dto.getCode());
        threadsRepository.delete(thread.getId());
    }
    
    public void create(final ThreadDto dto) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    createThreadImpl(dto);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void createThreadImpl(ThreadDto dto) {
        Thread thread = mapper.map(dto, Thread.class);
        threadsRepository.save(thread);
    }

    public List<ThreadDto> listThreads() {
        return transactionTemplate.execute(new TransactionCallback<List<ThreadDto>>() {
            public List<ThreadDto> doInTransaction(TransactionStatus status) {
                try {
                    return listThreadsImpl();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private List<ThreadDto> listThreadsImpl() {
        List<ThreadDto> result = new ArrayList<ThreadDto>();
        Iterable<Thread> threads = threadsRepository.findAll();
        for (Thread t : threads) {
            result.add(mapper.map(t, ThreadDto.class));
        }
        return result;
    }

    public void update(final ThreadUpdateDto dto) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    updateThreadImpl(dto);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
    
    private void updateThreadImpl(ThreadUpdateDto dto) {
        Thread thread = threadsRepository.findByCode(dto.getRef().getCode());
        mapper.map(dto.getDto(), thread);
        threadsRepository.save(thread);
    }

    public void create(final CanvasDto dto) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    createCanvasImpl(dto);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
    
    private void createCanvasImpl(CanvasDto dto) {
        Canvas canvas = mapper.map(dto, Canvas.class);
        canvasesRepository.save(canvas);
    }

    public void delete(final CanvasRefDto dto) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    deleteCanvasImpl(dto);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
    
    private void deleteCanvasImpl(CanvasRefDto dto) {
        Canvas canvas = canvasesRepository.findByCode(dto.getCode());
        canvasesRepository.delete(canvas);
    }

    public void update(final CanvasUpdateDto dto) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    updateCanvasImpl(dto);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
    
    private void updateCanvasImpl(CanvasUpdateDto dto) {
        Canvas canvas = canvasesRepository.findByCode(dto.getRef().getCode());
        mapper.map(dto.getDto(), canvas);
        canvasesRepository.save(canvas);
    }

    public List<CanvasDto> listCanvases() {
        return transactionTemplate.execute(new TransactionCallback<List<CanvasDto>>() {
            public List<CanvasDto> doInTransaction(TransactionStatus status) {
                try {
                    return listCanvasesImpl();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
    
    private List<CanvasDto> listCanvasesImpl() {
        List<CanvasDto> result = new ArrayList<CanvasDto>();
        Iterable<Canvas> canvases = canvasesRepository.findAll();
        for (Canvas c : canvases) {
            result.add(mapper.map(c, CanvasDto.class));
        }
        return result;
    }
    
}
