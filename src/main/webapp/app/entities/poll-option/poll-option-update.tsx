import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getPostPolls } from 'app/entities/post-poll/post-poll.reducer';
import { createEntity, getEntity, reset, updateEntity } from './poll-option.reducer';

export const PollOptionUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const postPolls = useAppSelector(state => state.postPoll.entities);
  const pollOptionEntity = useAppSelector(state => state.pollOption.entity);
  const loading = useAppSelector(state => state.pollOption.loading);
  const updating = useAppSelector(state => state.pollOption.updating);
  const updateSuccess = useAppSelector(state => state.pollOption.updateSuccess);

  const handleClose = () => {
    navigate(`/poll-option${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPostPolls({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }

    const entity = {
      ...pollOptionEntity,
      ...values,
      poll: postPolls.find(it => it.id.toString() === values.poll?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...pollOptionEntity,
          poll: pollOptionEntity?.poll?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="monsterdamChicksApp.pollOption.home.createOrEditLabel" data-cy="PollOptionCreateUpdateHeading">
            <Translate contentKey="monsterdamChicksApp.pollOption.home.createOrEditLabel">Create or edit a PollOption</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="poll-option-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('monsterdamChicksApp.pollOption.optionDescription')}
                id="poll-option-optionDescription"
                name="optionDescription"
                data-cy="optionDescription"
                type="textarea"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="poll-option-poll"
                name="poll"
                data-cy="poll"
                label={translate('monsterdamChicksApp.pollOption.poll')}
                type="select"
              >
                <option value="" key="0" />
                {postPolls
                  ? postPolls.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/poll-option" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default PollOptionUpdate;
