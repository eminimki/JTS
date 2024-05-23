package eminimki.com.JTS.Common.Utilities.Mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public interface ModelMapperService {

    ModelMapper forResponse();

    ModelMapper forRequest();

}
