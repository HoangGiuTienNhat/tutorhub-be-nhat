package com.yourcompany.yourproject.controller;

import com.yourcompany.yourproject.dto.GroupJoinRequestDto;
import com.yourcompany.yourproject.dto.GroupRequestDto;
import com.yourcompany.yourproject.dto.GroupResponseDto;
import com.yourcompany.yourproject.dto.UserDto;
import com.yourcompany.yourproject.service.GroupService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @PostMapping
    public ResponseEntity<GroupResponseDto> createGroup(@Valid @RequestBody GroupRequestDto groupRequestDto) {
        log.info("GroupController.createGroup called with: {}", groupRequestDto);
        GroupResponseDto createdGroup = groupService.createGroup(groupRequestDto);
        log.info("Group created successfully: {}", createdGroup);
        return new ResponseEntity<>(createdGroup, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<GroupResponseDto>> getAllGroups() {
        List<GroupResponseDto> groups = groupService.getAllGroups();
        return ResponseEntity.ok(groups);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupResponseDto> getGroupById(@PathVariable Long id) {
        GroupResponseDto group = groupService.getGroupById(id);
        return ResponseEntity.ok(group);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroupResponseDto> updateGroup(@PathVariable Long id,
            @Valid @RequestBody GroupRequestDto groupRequestDto) {
        GroupResponseDto updatedGroup = groupService.updateGroup(id, groupRequestDto);
        return ResponseEntity.ok(updatedGroup);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        groupService.deleteGroup(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/join")
    public ResponseEntity<Void> joinGroup(@Valid @RequestBody GroupJoinRequestDto groupJoinRequestDto) {
        groupService.joinGroups(groupJoinRequestDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{groupId}/users")
    public ResponseEntity<List<UserDto>> getUsersInGroup(@PathVariable Long groupId) {
        List<UserDto> users = groupService.getUsersInGroup(groupId);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/my-groups")
    public ResponseEntity<List<GroupResponseDto>> getMyGroups() {
        List<GroupResponseDto> groups = groupService.getJoinedGroupsForCurrentUser();
        return ResponseEntity.ok(groups);
    }

    @GetMapping("/my-created-groups")
    public ResponseEntity<List<GroupResponseDto>> getMyCreatedGroups() {
        List<GroupResponseDto> groups = groupService.getCreatedGroupsForCurrentUser();
        return ResponseEntity.ok(groups);
    }
}
