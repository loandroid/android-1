/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.tools.idea.tests.gui.framework.fixture.newProjectWizard;

import com.android.tools.idea.tests.gui.framework.fixture.ComponentFixture;
import com.android.tools.idea.tests.gui.framework.matcher.Matchers;
import com.android.tools.idea.wizard.dynamic.DynamicWizard;
import org.fest.swing.core.GenericTypeMatcher;
import org.fest.swing.core.Robot;
import org.fest.swing.core.matcher.JLabelMatcher;
import org.fest.swing.fixture.ContainerFixture;
import org.fest.swing.fixture.JButtonFixture;
import org.fest.swing.fixture.JLabelFixture;
import org.fest.swing.fixture.JTextComponentFixture;
import org.fest.swing.timing.Wait;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.text.JTextComponent;

import static com.android.tools.idea.tests.gui.framework.GuiTests.*;

/**
 * Base class for fixtures which control wizards that extend {@link DynamicWizard}
 */
public abstract class AbstractWizardFixture<S> extends ComponentFixture<S, JDialog> implements ContainerFixture<JDialog> {

  public AbstractWizardFixture(@NotNull Class<S> selfType, @NotNull Robot robot, @NotNull JDialog target) {
    super(selfType, robot, target);
  }

  @NotNull
  protected JRootPane findStepWithTitle(@NotNull final String title) {
    JRootPane rootPane = target().getRootPane();
    waitUntilShowing(robot(), rootPane, JLabelMatcher.withText(title));
    return rootPane;
  }

  @NotNull
  public S clickNext() {
    findAndClickButtonWhenEnabled(this, "Next");
    return myself();
  }

  @NotNull
  public S clickFinish() {
    findAndClickButtonWhenEnabled(this, "Finish");
    Wait.seconds(5).expecting("dialog to disappear").until(() -> !target().isShowing());
    return myself();
  }

  @NotNull
  public S clickCancel() {
    findAndClickCancelButton(this);
    Wait.seconds(5).expecting("dialog to disappear").until(() -> !target().isShowing());
    return myself();
  }

  @NotNull
  public S clickPrevious() {
    findAndClickButtonWhenEnabled(this, "Previous");
    return myself();
  }

  @NotNull
  public JTextComponentFixture findTextField(@NotNull final String labelText) {
    return new JTextComponentFixture(robot(), robot().finder().findByLabel(labelText, JTextComponent.class));
  }

  @NotNull
  public JButtonFixture findWizardButton(@NotNull final String text) {
    JButton button = robot().finder().find(target(), Matchers.byText(JButton.class, text));
    return new JButtonFixture(robot(), button);
  }

  @NotNull
  public JLabelFixture findLabel(@NotNull final String text) {
    JLabel label = waitUntilFound(robot(), target(), new GenericTypeMatcher<JLabel>(JLabel.class) {
      @Override
      protected boolean isMatching(@NotNull JLabel label) {
        return text.equals(label.getText().replaceAll("(?i)<.?html>", ""));
      }
    });

    return new JLabelFixture(robot(), label);
  }
}
