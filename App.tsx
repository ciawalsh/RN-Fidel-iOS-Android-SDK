/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow
 */

import React, {Component} from 'react';
import {Platform, NativeModules, StyleSheet, Text, TouchableOpacity, View} from 'react-native';

const instructions = Platform.select({
  ios: 'Press Cmd+R to reload,\n' + 'Cmd+D or shake for dev menu',
  android:
    'Double tap R on your keyboard to reload,\n' +
    'Shake or press menu button for dev menu',
});

const CalanderManager = NativeModules.CalanderManager

type Props = {};
export default class App extends Component<Props> {
  render() {
    return (
      <View style={styles.container}>
        <Text style={styles.welcome}>Welcome to React Native!</Text>
        <Text style={styles.instructions}>To get started, edit App.js</Text>
        <TouchableOpacity onPress={this.presentFidel}>
          <Text>Touch Me</Text>
        </TouchableOpacity>
      </View>
    );
  }

  setMetaData = (userEmail) => {
    CalanderManager.setMetaData(userEmail);
  }

  presentFidel = async () => {
    try {
      this.setMetaData("a@b.c");
      var present = await CalanderManager.present();
      console.log("success: ", present);
      return present;
    }
    catch (e) {
      console.log("error: ", e)
    }
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
});
