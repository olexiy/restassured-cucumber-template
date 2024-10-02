package de.olexiy.examples.apitesting.restassured.config;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import io.cucumber.plugin.EventListener;
import io.cucumber.plugin.event.*;

public class TestListener implements EventListener {

    private ExtentReports extent;
    private ExtentTest scenario;

    public TestListener() {
        extent = new ExtentReports();
        ExtentSparkReporter spark = new ExtentSparkReporter("target/Spark.html");
        extent.attachReporter(spark);
    }

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestRunStarted.class, this::runStarted);
        publisher.registerHandlerFor(TestRunFinished.class, this::runFinished);
        publisher.registerHandlerFor(TestCaseStarted.class, this::scenarioStarted);
        publisher.registerHandlerFor(TestCaseFinished.class, this::scenarioFinished);
        publisher.registerHandlerFor(TestStepStarted.class, this::stepStarted);
        publisher.registerHandlerFor(TestStepFinished.class, this::stepFinished);
    }

    private void runStarted(TestRunStarted event) {
        System.out.println("Test run started");
    }

    private void runFinished(TestRunFinished event) {
        System.out.println("Test run finished");
        extent.flush();
    }

    private void scenarioStarted(TestCaseStarted event) {
        String scenarioName = event.getTestCase().getName();
        scenario = extent.createTest(scenarioName);
    }

    private void scenarioFinished(TestCaseFinished event) {
        Status status = Status.PASS;
        if (event.getResult().getStatus().is(io.cucumber.plugin.event.Status.FAILED)) {
            status = Status.FAIL;
        } else if (event.getResult().getStatus().is(io.cucumber.plugin.event.Status.SKIPPED)) {
            status = Status.SKIP;
        }
        scenario.log(status, "Scenario " + status.toString().toLowerCase());
    }

    private void stepStarted(TestStepStarted event) {
        if (event.getTestStep() instanceof PickleStepTestStep) {
            PickleStepTestStep pickleStep = (PickleStepTestStep) event.getTestStep();
            scenario.log(Status.INFO, pickleStep.getStep().getText());
        }
    }

    private void stepFinished(TestStepFinished event) {
        if (event.getTestStep() instanceof PickleStepTestStep) {
            PickleStepTestStep pickleStep = (PickleStepTestStep) event.getTestStep();
            Status status = Status.PASS;
            if (event.getResult().getStatus().is(io.cucumber.plugin.event.Status.FAILED)) {
                status = Status.FAIL;
                scenario.log(status, event.getResult().getError().getMessage());
            } else if (event.getResult().getStatus().is(io.cucumber.plugin.event.Status.SKIPPED)) {
                status = Status.SKIP;
            }
            scenario.log(status, pickleStep.getStep().getText() + " " + status.toString().toLowerCase());
        }
    }
}
