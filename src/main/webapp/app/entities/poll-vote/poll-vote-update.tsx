import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getPollOptions } from 'app/entities/poll-option/poll-option.reducer';
import { createEntity, getEntity, reset, updateEntity } from './poll-vote.reducer';

export const PollVoteUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const pollOptions = useAppSelector(state => state.pollOption.entities);
  const pollVoteEntity = useAppSelector(state => state.pollVote.entity);
  const loading = useAppSelector(state => state.pollVote.loading);
  const updating = useAppSelector(state => state.pollVote.updating);
  const updateSuccess = useAppSelector(state => state.pollVote.updateSuccess);

  const handleClose = () => {
    navigate(`/poll-vote${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPollOptions({}));
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
    values.createdDate = convertDateTimeToServer(values.createdDate);
    if (values.votingUserId !== undefined && typeof values.votingUserId !== 'number') {
      values.votingUserId = Number(values.votingUserId);
    }

    const entity = {
      ...pollVoteEntity,
      ...values,
      pollOption: pollOptions.find(it => it.id.toString() === values.pollOption?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          createdDate: displayDefaultDateTime(),
        }
      : {
          ...pollVoteEntity,
          createdDate: convertDateTimeFromServer(pollVoteEntity.createdDate),
          pollOption: pollVoteEntity?.pollOption?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="monsterdamChicksApp.pollVote.home.createOrEditLabel" data-cy="PollVoteCreateUpdateHeading">
            <Translate contentKey="monsterdamChicksApp.pollVote.home.createOrEditLabel">Create or edit a PollVote</Translate>
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
                  id="poll-vote-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('monsterdamChicksApp.pollVote.createdDate')}
                id="poll-vote-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.pollVote.votingUserId')}
                id="poll-vote-votingUserId"
                name="votingUserId"
                data-cy="votingUserId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                id="poll-vote-pollOption"
                name="pollOption"
                data-cy="pollOption"
                label={translate('monsterdamChicksApp.pollVote.pollOption')}
                type="select"
              >
                <option value="" key="0" />
                {pollOptions
                  ? pollOptions.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/poll-vote" replace color="info">
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

export default PollVoteUpdate;
