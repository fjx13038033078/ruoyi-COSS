package com.ruoyi.web.controller.community;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.community.domain.CommMatter;
import com.ruoyi.community.service.ICommMatterService;

@RestController
@RequestMapping
public class CommMatterController extends BaseController
{
    @Autowired
    private ICommMatterService matterService;

    @PreAuthorize("@ss.hasPermi('community:matter:list')")
    @GetMapping("/community/matter/list")
    public TableDataInfo adminList(CommMatter matter)
    {
        startPage();
        List<CommMatter> list = matterService.selectCommMatterList(matter);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('community:matter:query')")
    @GetMapping("/community/matter/{matterId}")
    public AjaxResult adminGet(@PathVariable Long matterId)
    {
        return success(matterService.selectCommMatterById(matterId));
    }

    @PreAuthorize("@ss.hasPermi('community:matter:add')")
    @Log(title = "matter", businessType = BusinessType.INSERT)
    @PostMapping("/community/matter")
    public AjaxResult add(@Validated @RequestBody CommMatter matter)
    {
        matter.setCreateBy(SecurityUtils.getUsername());
        return toAjax(matterService.insertCommMatter(matter));
    }

    @PreAuthorize("@ss.hasPermi('community:matter:edit')")
    @Log(title = "matter", businessType = BusinessType.UPDATE)
    @PutMapping("/community/matter")
    public AjaxResult edit(@Validated @RequestBody CommMatter matter)
    {
        matter.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(matterService.updateCommMatter(matter));
    }

    @PreAuthorize("@ss.hasPermi('community:matter:remove')")
    @Log(title = "matter", businessType = BusinessType.DELETE)
    @DeleteMapping("/community/matter/{matterIds}")
    public AjaxResult remove(@PathVariable Long[] matterIds)
    {
        return toAjax(matterService.deleteCommMatterByIds(matterIds));
    }

    @PreAuthorize("@ss.hasRole('resident')")
    @GetMapping("/portal/matter/list")
    public TableDataInfo portalList(CommMatter matter)
    {
        matter.setStatus("0");
        startPage();
        return getDataTable(matterService.selectCommMatterList(matter));
    }

    @PreAuthorize("@ss.hasRole('resident')")
    @GetMapping("/portal/matter/{matterId}")
    public AjaxResult portalGet(@PathVariable Long matterId)
    {
        CommMatter m = matterService.selectCommMatterById(matterId);
        if (m == null || !"0".equals(m.getStatus()))
        {
            return error("matter not available");
        }
        return success(m);
    }

    @PreAuthorize("@ss.hasRole('resident')")
    @GetMapping("/portal/matter/hot")
    public AjaxResult hotMatters(@RequestParam(value = "limit", defaultValue = "8") int limit)
    {
        return success(matterService.selectHotMatters(limit));
    }
}
