package com.yd.price.workflow.cucumber.mapper;

import com.yd.price.workflow.cucumber.storage.DataFolderStorage;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.DataTableType;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

public class DataFolderMapper {

  @DataTableType
  public DataFolderStorage.DataFolder map(DataTable dataTable) {
    List<Map<String, String>> entries = dataTable.transpose().entries();
    Assert.notEmpty(entries, "Entries must not be empty");

    Map<String, String> map = entries.get(0);
    Assert.notEmpty(map, "map must not be empty");

    var rest = new DataFolderStorage.DataFolder.DataPath(map.get("INPUT_REQUEST_PATH"), map.get("OUTPUT_RESPONSE_PATH"));

    return new DataFolderStorage.DataFolder(rest);
  }
}
