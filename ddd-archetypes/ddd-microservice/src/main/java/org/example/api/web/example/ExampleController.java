package org.example.api.web.example;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.application.example.dto.command.ExampleCreateCommand;
import org.example.application.example.dto.command.ExampleUpdatePasswordCommand;
import org.example.application.example.dto.query.ExampleListQuery;
import org.example.application.example.dto.query.ExamplePageQuery;
import org.example.application.example.dto.result.ExampleResult;
import org.example.application.example.service.ExampleCommandService;
import org.example.application.example.service.ExampleQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/web/example")
public class ExampleController {
    @Autowired
    private ExampleQueryService exampleQueryService;

    @Autowired
    private ExampleCommandService exampleCommandService;

    @GetMapping("/page")
    public Page<ExampleResult> page(@Valid ExamplePageQuery query) {
        return exampleQueryService.page(query);
    }

    @GetMapping("/list")
    public List<ExampleResult> list(@Valid ExampleListQuery query) {
        return exampleQueryService.list(query);
    }

    @GetMapping("/{id}")
    public ExampleResult detail(@PathVariable Long id) {
        return exampleQueryService.detail(id);
    }

    @PostMapping
    public void create(@Valid @RequestBody ExampleCreateCommand command) {
        exampleCommandService.create(command);
    }

    @PutMapping("/password")
    public void updatePassword(@Valid @RequestBody ExampleUpdatePasswordCommand command) {
        exampleCommandService.update(command);
    }

    @PutMapping("/{id}/enabled")
    public void enable(@PathVariable Long id) {
        exampleCommandService.enable(id);
    }

    @PutMapping("/{id}/disabled")
    public void disable(@PathVariable Long id) {
        exampleCommandService.disable(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        exampleCommandService.delete(id);
    }
}
