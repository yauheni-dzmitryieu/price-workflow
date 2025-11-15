package com.yd.price.workflow.cucumber.storage;

import io.cucumber.spring.ScenarioScope;
import org.springframework.stereotype.Component;


@Component
@ScenarioScope
public class DataFolderStorage {

    private DataFolder dataFolder;

    public DataFolder getDataFolder() {
        return dataFolder;
    }

    public void setDataFolder(DataFolder dataFolder) {
        this.dataFolder = dataFolder;
    }

    public record DataFolder(DataPath rest) {

        public record DataPath(
                String input,
                String output) {
        }
    }
}
