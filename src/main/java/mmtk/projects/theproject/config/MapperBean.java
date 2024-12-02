package mmtk.projects.theproject.config;

import org.apache.catalina.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Author : Min Myat Thu Kha
 * Created At : 06/12/2024, Dec , 16:01
 * Project Name : TheProject
 **/
@Configuration
public class MapperBean {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
