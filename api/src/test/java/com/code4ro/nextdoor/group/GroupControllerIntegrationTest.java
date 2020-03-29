package com.code4ro.nextdoor.group;

import com.code4ro.nextdoor.core.AbstractControllerIntegrationTest;
import com.code4ro.nextdoor.core.RandomObjectFiller;
import com.code4ro.nextdoor.group.dto.GroupCreateDto;
import com.code4ro.nextdoor.group.dto.GroupDto;
import com.code4ro.nextdoor.group.dto.GroupUpdateDto;
import com.code4ro.nextdoor.group.entity.Group;
import com.code4ro.nextdoor.group.repository.GroupRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GroupControllerIntegrationTest extends AbstractControllerIntegrationTest {
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Transactional
    @SneakyThrows
    @Test
    @WithMockUser
    public void createGroup() {
        final GroupCreateDto createDto = RandomObjectFiller.createAndFill(GroupCreateDto.class);

        mvc.perform(post(endpoint("/api/groups/"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Transactional
    @SneakyThrows
    @Test
    @WithMockUser
    public void updateGroup() {
        final GroupUpdateDto updateDto = RandomObjectFiller.createAndFill(GroupUpdateDto.class);
        final Group group = groupRepository.save(RandomObjectFiller.createAndFill(Group.class));

        final MvcResult mvcResult = mvc.perform(put(endpoint("/api/groups/" + group.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        final GroupDto groupDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), GroupDto.class);
        assertThat(groupDto.getName()).isEqualTo(updateDto.getName());
        assertThat(group.getDescription()).isEqualTo(updateDto.getDescription());
        assertThat(group.getOpen()).isEqualTo(updateDto.getOpen());
    }

    @SneakyThrows
    @Test
    @WithMockUser
    public void getGroup() {
        final Group group = groupRepository.save(RandomObjectFiller.createAndFill(Group.class));

        final MvcResult mvcResult = mvc.perform(get(endpoint("/api/groups/" + group.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        final GroupDto groupDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), GroupDto.class);
        assertThat(groupDto.getName()).isEqualTo(group.getName());
        assertThat(groupDto.getDescription()).isEqualTo(group.getDescription());
        assertThat(groupDto.getOpen()).isEqualTo(group.getOpen());
    }
}
