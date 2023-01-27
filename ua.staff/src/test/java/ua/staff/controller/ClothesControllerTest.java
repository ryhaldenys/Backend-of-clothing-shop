package ua.staff.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.test.web.servlet.MockMvc;

import ua.staff.dto.ClothesDetailsDto;
import ua.staff.dto.ClothesDto;
import ua.staff.dto.FullClothesDto;
import ua.staff.generator.ClothesGenerator;
import ua.staff.model.Clothes;
import ua.staff.model.ClothesRequestParams;
import ua.staff.model.Image;
import ua.staff.model.Size;
import ua.staff.service.ClothesService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.core.Is.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WithMockUser
@WebMvcTest(ClothesController.class)
public class ClothesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClothesService service;

    @Test
    void getAllTest() throws Exception {

        List<ClothesDto> clothesDtos = List.of(
                new ClothesDto(1L,"hoodie", BigDecimal.valueOf(1000),"hoodie/black","")
        );

        Slice<ClothesDto> clothesDtoSlice = new PageImpl<>(clothesDtos);

        when(service.getClothesDtos(any(ClothesRequestParams.class), any(Pageable.class)))
                .thenReturn(clothesDtoSlice);

        mockMvc.perform(get("/clothes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name", is("hoodie")))
                .andExpect(jsonPath("$.content[0].id", is(1)));
    }

    @Test
    void getClothesByIdTest() throws Exception{
        when(service.getClothesById(1L))
                .thenReturn(
                        new FullClothesDto(
                            new ClothesDetailsDto(1L,"name","article",
                                    "subType","description", BigDecimal.valueOf(100),BigDecimal.valueOf(0.1)),
                            Set.of(new Image(true,"url")), Set.of(new Size("42",30)))
                );

        mockMvc.perform(get("/clothes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clothesDetails.name", is("name")))
                .andExpect(jsonPath("$.clothesDetails.id", is(1)))
                .andExpect(jsonPath("$.clothesDetails.article", is("article")));
    }


    @Test
    void saveClothes() throws Exception {
        var clothes = ClothesGenerator.generateClothes(12);
        clothes.setId(1L);

        mockMvc.perform(post("/clothes").with(csrf())
                        .content(mapToString(clothes))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(r -> assertThat(r.getResponse().getRedirectedUrl())
                        .isEqualTo("http://localhost/clothes/"+clothes.getId()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(clothes.getName())))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.article", is(clothes.getArticle())));
    }

    @Test
    void deleteClothes() throws Exception{
        //BDDMockito.willThrow(NotFoundException.class).given(service).deleteClothesById(1L);
        //BDDMockito.willThrow(NotFoundException.class);
        mockMvc.perform(delete("/clothes/1").with(csrf()))
                .andDo(print())
                .andExpect(r -> assertThat(r.getResponse().getRedirectedUrl())
                        .isEqualTo("http://localhost/clothes"))
                .andExpect(status().isNoContent());
    }

    private String mapToString(Clothes clothes) throws JsonProcessingException {
        ObjectMapper mapper = JsonMapper.builder()
                .findAndAddModules()
                .build();

        return mapper.writeValueAsString(clothes);
    }
}
